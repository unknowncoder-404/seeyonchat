//package com.seeyon.chat.action;
//
//import com.intellij.openapi.actionSystem.ActionUpdateThread;
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.progress.ProgressManager;
//import com.intellij.openapi.project.Project;
//import com.seeyon.chat.task.TestCaseTask;
//import org.jetbrains.annotations.NotNull;
//
///**
// * @author Shaozz
// */
//public class PromptTestCaseAction extends AnAction {
//
//
//    @Override
//    public @NotNull ActionUpdateThread getActionUpdateThread() {
//        return ActionUpdateThread.BGT;
//    }
//
//
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//
//        Project project = e.getProject();
//        // 启动异步任务
//        ProgressManager.getInstance().run(new TestCaseTask( e,));
//
//    }
//
//
//    @Override
//    public void update(@NotNull AnActionEvent e) {
//        // 确保动作在编辑器上下文中始终可用
//        e.getPresentation().setEnabledAndVisible(true);
////        e.getPresentation().setEnabled(!ChatService.getInstance(e.getProject()).isLocked());
//    }
//}
