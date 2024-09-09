package com.seeyon.chat.core.service;

import com.seeyon.chat.common.ChatConstants;
import com.seeyon.chat.exception.BuzinessException;
import com.seeyon.chat.utils.AzureHttpUtil;
import com.seeyon.chat.utils.JsonUtil;
import com.seeyon.chat.utils.NotificationUtil;
import com.seeyon.chat.utils.StringLLMUtil;

//@Service(Service.Level.PROJECT)
public final  class LLMCallService {
//    private final Project project;
//
//    public LLMCallService(Project project) {
//        this.project = project;
//
//    }
//    public static LLMCallService getInstance(@NotNull Project project) {
//        return project.getService(LLMCallService.class);
//    }
    /**
     *
     * @param data 请求大模型prompt
     * @param systemTemplate 请求大模型system模板
     * @param isSimpleFormat 返回结果是否json或代码
     * @return
     */
    public static String call(String systemTemplate, String data,Boolean isSimpleFormat) throws BuzinessException{

        try {
            long start = System.currentTimeMillis();
            String message = AzureHttpUtil.sendMessage(systemTemplate,data);
            String azureMessage = JsonUtil.getAzureMessage(message);
            long end = System.currentTimeMillis();
            if(ChatConstants.isDebug){
                long takeSeconds = (end-start);
                System.out.println("\n\ncall llm speed："+takeSeconds+"ms\n");
                System.out.println("\ncall content:"+azureMessage+"\n\n");
            }
            //去除 代码块
            azureMessage = StringLLMUtil.extractCodeBlock(azureMessage);

            return azureMessage;

        } catch (Exception e) {

            NotificationUtil.error(e.getMessage());
            throw new BuzinessException(e.getMessage());
        }

    }




}
