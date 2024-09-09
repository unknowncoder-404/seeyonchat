package com.seeyon.chat.task;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.seeyon.chat.core.service.LLMCallService;
import com.seeyon.chat.exception.BuzinessException;
import com.seeyon.chat.settings.AppSettingsState;
import com.seeyon.chat.utils.FileUtil;
import com.seeyon.chat.utils.JsonUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCaseTask extends Task.Backgroundable {

    private final AnActionEvent e;
    private final String outputFolder;

    // 构造函数接收 AnActionEvent 对象
    public TestCaseTask( @NotNull AnActionEvent actionEvent,String outputFolder) {
        super(actionEvent.getProject(), "TestCaseTask");
        this.e = actionEvent;
        this.outputFolder = outputFolder;
    }



    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        actionPerformed(indicator);
    }

    public void actionPerformed(@NotNull ProgressIndicator indicator) {
        indicator.setFraction(10 / 100.0); // 更新进度

        Project project = e.getProject();


        // 获取当前文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null || !(psiFile instanceof PsiJavaFile)) {
            return;
        }

        // 获取文件内容
        String selectedText = psiFile.getText();

        try {
            FileUtil.save2TestFolder(outputFolder, "origin.json", selectedText, e, true);
        } catch (BuzinessException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

//        ChatToolWindowFactory.showToolWindow(project);
//        Editor editor = e.getData(CommonDataKeys.EDITOR);
//        assert editor != null;
//        String selectedText = editor.getSelectionModel().getSelectedText();
//        if (selectedText == null || selectedText.isEmpty()) {
//            Document document = editor.getDocument();
//            selectedText = document.getText();
//        }


        //拆解java代码转变为json.
        String splitCodeJson = null;
        try {

            String spliteCodePrompt = AppSettingsState.getInstance().getGptSplitCodePrompt();
            String spliteCodeSystemPrompt = "";

            String spliteCodeContent = spliteCodePrompt.replace("#{CODE}",selectedText) ;

            splitCodeJson = LLMCallService.call(spliteCodeSystemPrompt,spliteCodeContent,true);
        } catch (BuzinessException ex) {
            return;
        }
        //任务终止
        if (indicator.isCanceled()) {
            // 任务被取消时的处理逻辑
            return;
        }
        indicator.setFraction(40 / 100.0); // 更新进度

        //合并json结构
        String jsonMergeContent = null;
        try {
            String jsonMergePrompt = AppSettingsState.getInstance().getGptCodeMergePrompt();
            String jsonMergeSystemPrompt = "";

            jsonMergePrompt = jsonMergePrompt.replace("#{CODE}",splitCodeJson) ;

            jsonMergeContent = LLMCallService.call(jsonMergeSystemPrompt,jsonMergePrompt,true);
        } catch (BuzinessException ex) {
            return;
        }

        //任务终止
        if (indicator.isCanceled()) {
            // 任务被取消时的处理逻辑
            return;
        }
        indicator.setFraction(60 / 100.0); // 更新进度


        //遍历方法
        String classContent = JsonUtil.getClassContent(jsonMergeContent);
        String classImportList = JsonUtil.getClassImport(jsonMergeContent);
        String className = JsonUtil.getClassName(jsonMergeContent);
        String packagePath = JsonUtil.getClassPackage(jsonMergeContent);
        String packagePathFold = packagePath.replace(".","/");
        String mergedCode = null;
        List<String> methodNameList = JsonUtil.getMethodNameList(jsonMergeContent);
        List<String> methodContentList = JsonUtil.getMethodContentList(jsonMergeContent);
        //遍历方法classImport
        Map<String,String> classImportJsonMap = new HashMap<>();

        //TODO: 为了观察拆分情况, 后期删除
        try {
            FileUtil.save2TestFolder(outputFolder, "origin.json", jsonMergeContent, e, true);
        } catch (BuzinessException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

//        List<String> extractClassNameList = new ArrayList<>();
//        //TODO: 暂时不考虑增加import
//        if (classImportList != null) {
//            String[] classImportArr = classImportList.split(",");
//            for(String classImport : classImportArr){
//                try {
//                    String classInfoAsJson = ClassInfoExtractor.getClassInfoJSON(project,classImport);
//                    String extractClassName = PsiFileRetrieverUtil.extractClassName(classImport);
//                    extractClassNameList.add(extractClassName);
//                    classImportJsonMap.put(extractClassName,classInfoAsJson);
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
////                String importName = PsiFileRetrieverUtil.extractClassName(classImport);
////                CodeUtil.extractClassInfo(project,PsiFileRetrieverUtil.getPsiFile(project,classImport),importName);
//            }
//        }
        int methodContentSize = methodContentList.size();
        for (int i = 0; i< methodContentSize; i++) {
            String methodInfo = methodContentList.get(i);
            String methodName = methodNameList.get(i);

            System.out.println("单元测试正在生成:"+methodName+",进度:"+(i+1)+"/"+methodContentSize);
//            List<String> methodImportJsonList = new ArrayList<>();
//            for (String extractClassName : extractClassNameList) {
//                if(methodInfo.contains(extractClassName)){
//                    methodImportJsonList.add(methodInfo);
//                }
//            }

            mergedCode = classContent.replace("#{methodCode}", methodInfo);



            String questionContent = mergedCode;

            //生成单元测试代码
            try {
                String testCaseCodeSystemTemplate = AppSettingsState.getInstance().getGptJavaCodeprompt();
                testCaseCodeSystemTemplate= testCaseCodeSystemTemplate.replace("#{TEST_CASE_FOLDER}",outputFolder);
                String testCaseCode = LLMCallService.call(testCaseCodeSystemTemplate,questionContent,true);
                //首字母大写
                methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                String fileName = null;
                if("##other##".equalsIgnoreCase(methodName)){
                    fileName = className + "Test.java";
                }else {
                    fileName = className + methodName + "Test.java";
                }

                //保存到java单元测试文件
                FileUtil.save2TestFolder(outputFolder, fileName, testCaseCode, e, true);
            } catch (BuzinessException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            //任务终止
            if (indicator.isCanceled()) {
                // 任务被取消时的处理逻辑
                return;
            }
            int methodFraction = (40 / methodContentSize) * i;
            indicator.setFraction((60+methodFraction) / 100.0); // 更新进度

        }
        indicator.setFraction(1); // 更新进度
        System.out.println("单元测试生成完成");
        Messages.showInfoMessage(project, "单元测试已生成!", "单元测试");
    }


}
