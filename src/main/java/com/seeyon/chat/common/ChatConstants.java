package com.seeyon.chat.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Shaozz
 */
public class ChatConstants {

    public static final Boolean isDebug = true;

    public static final String BASE_URL = "https://seeyon.chat/api";

    public static final Map<String, String> MODEL_MAP = new LinkedHashMap<>();

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();



//    public static final String SPLITE_CODE_SYSTEM_TEMPLATE =
//            "## 背景知识: \n" +
//                    "\n" +
//                    "\t你是一个java程序员, 你的工作是按照java编译规则, 将javaCode代码 转换为 json结构. \n" +
//                    "\n" +
//                    "\tjson结构如下:\n" +
//                    "\t\n" +
//                    "\t\t{\n" +
//                    "\t\t\"packagePath\":\"com.seeyon.demo.order.domain.service\",\n" +
//                    "\t\t\"className\":\"OrderInfoService\",\n" +
//                    "\t\t\"classImport\":\"com.seeyon.boot.domain.entity.BaseEntity,com.seeyon.boot.enums.ActionType\",\n" +
//                    "\t\t\"classInfo\":\"//类属性信息,类信息,包信息,import信息,注释信息\",\n" +
//                    "\t\t\"methodInfos\":[{\n" +
//                    "\t\t\tmethodName:\"create\",\n" +
//                    "\t\t\tmethodInfo:\"//方法信息,方法注释信息\"\n" +
//                    "\t\t },{\n" +
//                    "\t\t\tmethodName:\"##other##\",\n" +
//                    "\t\t\tmethodInfo:\"//多个少于15行代码的方法代码 和 注释信息\"\n" +
//                    "\t\t }]\n" +
//                    "\t\t}\n" +
//                    "\t\n" +
//                    "\t解释:\n" +
//                    "\t1. classInfo属性是类信息( 里面有一个保留字:\"#{替换method代码}\";用来后续替换成methodInfo代码);\n" +
//                    "\t2. classImport属性是所有import类中,以com.seeyon开头的类,多个类用逗号分割; \n" +
//                    "\t3. className属性是类名称\n" +
//                    "\t4. packagePath:获取代码的package信息 ; \n" +
//                    "\t5. methodInfos:是一个数组,只记录的public和protected方法,private方法直接跳过;\n" +
//                    "\n" +
//                    "\n" +
//                    "\n" +
//                    "## 处理逻辑:\n" +
//                    "\n" +
//                    "步骤1. 记录input代码中所有public方法内容,记录到 methodPublis字典. 字典key是否方法名称, value是方法代码 (如果存在同名方法, 则将方法代码合并).\n" +
//                    "步骤2. 遍历methodPublis字典 ,\n" +
//                    "if(value中 if条件分支数量少于4个){\n" +
//                    "\t将value放入methodName=\"##other##\"对应的methodInfo中\n" +
//                    "}else{\n" +
//                    "\t将key和value放入methodName和methodInfo中\n" +
//                    "}\n" +
//                    "\n" +
//                    "## 要求:\n" +
//                    "\t1. 输出的json对象中,methodInfos下需要包含javaCode中所有的public和protected方法.如果有遗漏,请补全.\n" +
//                    "\t2. 因为返回内容需要交给json解析器解析,所以返回内容禁止描述和解释,以及```,```json 等信息";

    public static final String SPLITE_CODE_TEMPLATE =
                    "你熟悉java代码规则, 你的任务是将java代码 转换为 json结构字符串.保留所有信息,不要用...省略  \n" +
                            "json结构如下:\n" +
                            "{\n" +
                            "\"classPackage\":\"\",\n" +
                            "\"className\":\"\",\n" +
                            "\"classImport\":\"\",\n" +
                            "\"classContent\":\"\",\n" +
                            "\"methodInfos\":[{\n" +
                            "\tmethodName:\"\",\n" +
                            "\tmethodParams:\"\",\n" +
                            "\tmethodContent:\"\",\n" +
                            "\trowCount:\"\",\n" +
                            "\tifConditionNum:\"\"\n" +
                            " }]\n" +
                            "}\n" +
                            "\n" +
                            "json结构解释:\n" +
                            "1. classPackage:获取代码的package信息 ; \n" +
                            "2. className属性是类名称\n" +
                            "3. classImport属性是所有import类中,以com.seeyon开头的类,多个类用逗号分割; \n" +
                            "4. classContent属性是原有java代码包括包信息,import信息,类信息,注释和注解信息,类属性信息,保留所有信息,不要用...省略. 并在类{}内部 添加一个字符串 \"#{methodCode}\";\n" +
                            "5. methodInfos:是一个数组,只记录的public和protected方法. private方法直接跳过;\n" +
                            "6. methodContent: 是方法信息,包括注释,方法内容等.保留所有信息,不要用...省略\n" +
                            "7. rowCount: 是方法代码有多少行\n" +
                            "8. ifConditionNum: 方法代码中有多少if和case分支\n" +
                            "\n" +
                            "请将下面java代码转成json结构字符串:\n" +
                            "#{CODE}";
    public static final String SPLITE_JSON_MERGE_TEMPLATE =
            "你的任务是将一个json字符串,按照json字符串合并规则,进行内部节点的合并,并输出合并后的json字符串. 保留所有信息,不要用...省略\n" +
                    "\n" +
                    "json字符串合并规则如下:\n" +
                    "规则1: 遍历methodInfos,如果当前对象的ifConditionNum<4 , 则methodContent值合并到 methodName=\"##other##\"对应的methodContent值中.合并对象之间间隔一个空行\n" +
                    "规则2: 合并过程尽量不要影响其他属性信息.\n" +
                    "\n" +
                    "输出格式要求:\n" +
                    "因为返回内容需要交给json解析器解析,所以返回内容禁止描述和解释,以及```,```json ,等信息\n" +
                    "并在处理完后,请仔细检查json字符串合并规则,如果不满足请重新生成\n" +
                    "\n" +
                    "请合并下面的json字符串进行合并处理:\n" +
                    "#{CODE}";

    public static final String GENERATE_TESTCASE_TEMPLATE =
            "## 背景知识: \n" +
                    "\t\n" +
                    "\t目标:\n" +
                    "\t```\n" +
                    "\u200B    输入: javaCode\n" +
                    "\t输出: 覆盖所有javaCode所有分支场景的 单元测试代码 \n" +
                    "\t```\n" +
                    "\t\n" +
                    "\t业务字典示例代码:\n" +
                    "\t```java\n" +
                    "\t\tclass MyClass\n" +
                    "\t\t\t/**\n" +
                    "\t\t\t* @param id: EntityObj对象id\n" +
                    "\t\t\t*/\n" +
                    "\t\t\tpublic int demoMethod(Long id) {\n" +
                    "\t\t\t\tEntityObj entity = getEntity(id);\n" +
                    "\t\t\t\tif (entity == null) { //分支场景a 实现覆盖此分支\n" +
                    "\t\t\t\t\t//\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tint count = orderInfoDao.findNum(entity.getCode());\n" +
                    "\t\t\t\tMessageProducer messageProducer = messageProducerService.getById(entity.getCode());\n" +
                    "\t\t\t\tif (count > 0 && messageProducer != null) { //分支场景b,分支场景c,分支场景d,分支场景e, 实现覆盖此分支\n" +
                    "\t\t\t\t\t// \n" +
                    "\t\t\t\t} \n" +
                    "\t\t\t\treturn count; \n" +
                    "\t\t\t}\t\n" +
                    "\t\t}\n" +
                    "\t```\n" +
                    "\t\n" +
                    "\t业务字典示例代码包含的分支场景:\n" +
                    "\t```\n" +
                    "\t\t分支场景a) entity == null \n" +
                    "        分支场景b) entity != null && count > 0 && messageProducer != null \n" +
                    "        分支场景c) entity != null && count == 0 && messageProducer != null \n" +
                    "        分支场景d) entity != null && count > 0 && messageProducer == null \n" +
                    "        分支场景e) entity != null && count == 0 && messageProducer == null \n" +
                    "\t```\n" +
                    "\t\n" +
                    "\t\n" +
                    "\t一个分支场景对应一个单元测试方法.\"业务字典示例代码\"中所有分支全部有对应的\"单元测试方法\",则表示覆盖了所有分支场景.\n" +
                    "\t\n" +
                    "\n" +
                    "\t\n" +
                    "\t其他常用对象信息:\n" +
                    "\n" +
                    "\t```java\n" +
                    "\tpublic class BusinessException extends BaseException {\n" +
                    "\t\tpublic BusinessException()\n" +
                    "\t\tpublic BusinessException(String code) \n" +
                    "\t\tpublic BusinessException(String code, String... params) \n" +
                    "\t\tpublic BusinessException(String code, Throwable cause)\n" +
                    "\t\tpublic BusinessException addError(String customMessage)\n" +
                    "\t\tpublic static BusinessException message(String customMessage)\n" +
                    "\t\tpublic String getMessage()\n" +
                    "\t\tpublic String code()\n" +
                    "\t\tpublic String message()\n" +
                    "\t\tpublic boolean hasCustomErrorMessage()\n" +
                    "\t}\n" +
                    "\n" +
                    "\t class SingleResponse<T> extends BaseResponse {\n" +
                    "\t\t@DtoAttribute(\"返回值数据\")\n" +
                    "\t\tprivate SingleData<T> data;\n" +
                    "\t\tpublic SingleResponse() \n" +
                    "\t\tpublic static <T> SingleResponse<T> from(T dto) \n" +
                    "\t\tpublic static <T> SingleResponse<T> ok()\n" +
                    "\t\tpublic static SingleResponse buildProcessingResponse()\n" +
                    "\t\tpublic SingleData<T> getData() \n" +
                    "\t\tpublic void setData(SingleData<T> data)\n" +
                    "\t}\n" +
                    "\n" +
                    "\tclass SingleData<T> {\n" +
                    "\t\t@DtoAttribute(\"数据对象\")\n" +
                    "\t\tprivate T content;\n" +
                    "\t\tpublic SingleData() \n" +
                    "\t\tpublic static <T> SingleData<T> from(T data)\n" +
                    "\t\tpublic T getContent()\n" +
                    "\t\tpublic void setContent(T content) \n" +
                    "\t}\n" +
                    "\t\n" +
                    "\t/**\n" +
                    "\t* 分页列表对象, 初始化对象时,需要设置content和pageInfo不能为空\n" +
                    "\t*/\n" +
                    "\tpublic class PageData<T> implements Serializable {\n" +
                    "\t\t@DtoAttribute(\"分页参数对象\")\n" +
                    "\t\tprivate PageInfo pageInfo;\n" +
                    "\t\t@DtoAttribute(\"数据集对象\")\n" +
                    "\t\tprivate List<T> content;\n" +
                    "\n" +
                    "\t\tpublic void setPageInfo(PageInfo pageInfo) ;\n" +
                    "\t\t\n" +
                    "\t\tpublic void setContent(List<T> content);\n" +
                    "\t}\n" +
                    "\t```\n" +
                    "\n" +
                    "\n" +
                    "## 示例:\n" +
                    "\n" +
                    "示例输入:\n" +
                    "```\n" +
                    "package com.seeyon.demo.order.domain.service;\n" +
                    "\n" +
                    "import com.seeyon.boot.enums.ActionType;\n" +
                    "import com.seeyon.boot.exception.BusinessException;\n" +
                    "import com.seeyon.boot.exception.ErrorCode;\n" +
                    "import com.seeyon.demo.order.domain.dao.OrderDetailDao;\n" +
                    "import com.seeyon.demo.order.domain.entity.OrderDetail;\n" +
                    "import com.seeyon.demo.order.dto.OrderDetailDto;\n" +
                    "import lombok.extern.slf4j.Slf4j;\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.cache.annotation.CacheConfig;\n" +
                    "import org.springframework.stereotype.Service;\n" +
                    "import org.springframework.util.CollectionUtils;\n" +
                    "\n" +
                    "import java.util.HashMap;\n" +
                    "import java.util.List;\n" +
                    "import java.util.Map;\n" +
                    "import java.util.concurrent.ExecutorService;\n" +
                    "\n" +
                    "@Service\n" +
                    "@CacheConfig(cacheNames = \"DemoInfo\")\n" +
                    "@Slf4j\n" +
                    "public class DemoInfoService {\n" +
                    "\n" +
                    "    @Autowired\n" +
                    "    private OrderDetailDao orderDetailDao;\n" +
                    "\n" +
                    "    @Autowired\n" +
                    "    private ExecutorService executorService;\n" +
                    "\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 获取OrderDetail列表详情\n" +
                    "     * @param entityId\n" +
                    "     * @param detailDtoList\n" +
                    "     * @return\n" +
                    "     */\n" +
                    "    public List<OrderDetail> doDetail(Long entityId, List<OrderDetailDto> detailDtoList) {\n" +
                    "\n" +
                    "        executorService.execute(new Runnable() {\n" +
                    "            @Override\n" +
                    "            public void run() {\n" +
                    "                log.info(\"线程池使用\");\n" +
                    "            }\n" +
                    "        });\n" +
                    "\n" +
                    "        List<OrderDetail> detailEntityList = null;\n" +
                    "        if (!CollectionUtils.isEmpty(detailDtoList)) {\n" +
                    "            Map<String, Object> map1 = new HashMap<>();\n" +
                    "            map1.put(\"orderInfoId\", entityId);\n" +
                    "            detailEntityList = orderDetailDao.selectListByConditions(map1, null);\n" +
                    "\n" +
                    "            Map<Long, OrderDetail> curDetailMap = new HashMap<>();\n" +
                    "            for (OrderDetail orderDetail : detailEntityList) {\n" +
                    "                curDetailMap.put(orderDetail.getId(), orderDetail);\n" +
                    "            }\n" +
                    "\n" +
                    "            for (OrderDetailDto orderDetailDto : detailDtoList) {\n" +
                    "                if (orderDetailDto.getActionType() == ActionType.INSERT) {\n" +
                    "                    OrderDetail insertDetail = new OrderDetail();\n" +
                    "                    insertDetail.from(orderDetailDto);\n" +
                    "                    orderDetailDao.create(insertDetail);\n" +
                    "                    detailEntityList.add(insertDetail);\n" +
                    "                } else if (orderDetailDto.getActionType() == ActionType.UPDATE) {\n" +
                    "                    OrderDetail updateDetail = curDetailMap.get(orderDetailDto.getId());\n" +
                    "                    if (updateDetail == null) {\n" +
                    "                        throw new BusinessException(ErrorCode.DB_OBJECT_NOT_EXIST);\n" +
                    "                    }\n" +
                    "                    updateDetail.from(orderDetailDto);\n" +
                    "                    orderDetailDao.update(updateDetail);\n" +
                    "                } else if (orderDetailDto.getActionType() == ActionType.DELETE) {\n" +
                    "                    OrderDetail deleteDetail = curDetailMap.get(orderDetailDto.getId());\n" +
                    "                    if (deleteDetail == null) {\n" +
                    "                        throw new BusinessException(ErrorCode.DB_OBJECT_NOT_EXIST);\n" +
                    "                    }\n" +
                    "                    orderDetailDao.deleteById(orderDetailDto.getId());\n" +
                    "                } else {\n" +
                    "                    log.warn(\"前端应该传递INSERT/UPDATE/DELETE状态的子对象，需要优化\");\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "        }\n" +
                    "        return detailEntityList;\n" +
                    "    }\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "```\n" +
                    "\n" +
                    "示例输出:\n" +
                    "```\n" +
                    "package com.seeyon.demo.order.domain.service;\n" +
                    "\n" +
                    "import com.seeyon.boot.enums.ActionType;\n" +
                    "import com.seeyon.boot.exception.BusinessException;\n" +
                    "import com.seeyon.demo.order.domain.dao.OrderDetailDao;\n" +
                    "import com.seeyon.demo.order.domain.entity.OrderDetail;\n" +
                    "import com.seeyon.demo.order.dto.OrderDetailDto;\n" +
                    "import org.junit.jupiter.api.BeforeEach;\n" +
                    "import org.junit.jupiter.api.Test;\n" +
                    "import org.mockito.ArgumentCaptor;\n" +
                    "import org.mockito.InjectMocks;\n" +
                    "import org.mockito.Mock;\n" +
                    "import org.mockito.MockitoAnnotations;\n" +
                    "\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.List;\n" +
                    "import java.util.concurrent.ExecutorService;\n" +
                    "\n" +
                    "import static org.junit.jupiter.api.Assertions.assertThrows;\n" +
                    "import static org.mockito.Mockito.*;\n" +
                    "\n" +
                    "class DemoInfoServiceDoDetailTest { \n" +
                    "\n" +
                    "    @Mock\n" +
                    "    private OrderDetailDao orderDetailDao;\n" +
                    "\n" +
                    "    @Mock\n" +
                    "    private ExecutorService executorService;\n" +
                    "\n" +
                    "    @InjectMocks\n" +
                    "    private DemoInfoService demoInfoService;\n" +
                    "\n" +
                    "    @BeforeEach\n" +
                    "    void setUp() {\n" +
                    "        MockitoAnnotations.openMocks(this);\n" +
                    "        System.setProperty(\"spring.profiles.active\", \"local\");\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithEmptyDetailDtoList() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        demoInfoService.doDetail(1L, detailDtoList);\n" +
                    "        verify(executorService, times(1)).execute(any(Runnable.class));\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithInsertAction() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        OrderDetailDto dto = new OrderDetailDto();\n" +
                    "        dto.setActionType(ActionType.INSERT);\n" +
                    "        detailDtoList.add(dto);\n" +
                    "\n" +
                    "        demoInfoService.doDetail(1L, detailDtoList);\n" +
                    "        verify(orderDetailDao, times(1)).create(any(OrderDetail.class));\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithUpdateActionAndDetailExists() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        OrderDetailDto dto = new OrderDetailDto();\n" +
                    "        dto.setId(1L);\n" +
                    "        dto.setActionType(ActionType.UPDATE);\n" +
                    "        detailDtoList.add(dto);\n" +
                    "\n" +
                    "        List<OrderDetail> existingDetails = new ArrayList<>();\n" +
                    "        OrderDetail existingDetail = new OrderDetail();\n" +
                    "        existingDetail.setId(1L);\n" +
                    "        existingDetails.add(existingDetail);\n" +
                    "\n" +
                    "        when(orderDetailDao.selectListByConditions(anyMap(), any())).thenReturn(existingDetails);\n" +
                    "\n" +
                    "        demoInfoService.doDetail(1L, detailDtoList);\n" +
                    "        verify(orderDetailDao, times(1)).update(any(OrderDetail.class));\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithUpdateActionAndDetailNotExists() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        OrderDetailDto dto = new OrderDetailDto();\n" +
                    "        dto.setId(1L);\n" +
                    "        dto.setActionType(ActionType.UPDATE);\n" +
                    "        detailDtoList.add(dto);\n" +
                    "\n" +
                    "        when(orderDetailDao.selectListByConditions(anyMap(), any())).thenReturn(new ArrayList<>());\n" +
                    "\n" +
                    "        assertThrows(BusinessException.class, () -> demoInfoService.doDetail(1L, detailDtoList));\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithDeleteActionAndDetailExists() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        OrderDetailDto dto = new OrderDetailDto();\n" +
                    "        dto.setId(1L);\n" +
                    "        dto.setActionType(ActionType.DELETE);\n" +
                    "        detailDtoList.add(dto);\n" +
                    "\n" +
                    "        List<OrderDetail> existingDetails = new ArrayList<>();\n" +
                    "        OrderDetail existingDetail = new OrderDetail();\n" +
                    "        existingDetail.setId(1L);\n" +
                    "        existingDetails.add(existingDetail);\n" +
                    "\n" +
                    "        when(orderDetailDao.selectListByConditions(anyMap(), any())).thenReturn(existingDetails);\n" +
                    "\n" +
                    "        demoInfoService.doDetail(1L, detailDtoList);\n" +
                    "        verify(orderDetailDao, times(1)).deleteById(1L);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithDeleteActionAndDetailNotExists() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        OrderDetailDto dto = new OrderDetailDto();\n" +
                    "        dto.setId(1L);\n" +
                    "        dto.setActionType(ActionType.DELETE);\n" +
                    "        detailDtoList.add(dto);\n" +
                    "\n" +
                    "        when(orderDetailDao.selectListByConditions(anyMap(), any())).thenReturn(new ArrayList<>());\n" +
                    "\n" +
                    "        assertThrows(BusinessException.class, () -> demoInfoService.doDetail(1L, detailDtoList));\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void testDoDetailWithUnhandledAction() {\n" +
                    "        List<OrderDetailDto> detailDtoList = new ArrayList<>();\n" +
                    "        OrderDetailDto dto = new OrderDetailDto();\n" +
                    "        dto.setActionType(null); // Unhandled action type\n" +
                    "        detailDtoList.add(dto);\n" +
                    "\n" +
                    "        demoInfoService.doDetail(1L, detailDtoList);\n" +
                    "        verify(executorService, times(1)).execute(any(Runnable.class));\n" +
                    "        // 模拟executorService.execute代码 线程池执行\n" +
                    "        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);\n" +
                    "        verify(executorService).execute(runnableCaptor.capture());\n" +
                    "        // Run the captured Runnable\n" +
                    "        Runnable capturedRunnable = runnableCaptor.getValue();\n" +
                    "        capturedRunnable.run();\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "}\n" +
                    "```\n" +
                    "\n" +
                    "## 输出内容:\n" +
                    "\t\n" +
                    "\t输出规则1: \"单元测试代码\"是一个可以直接保存为java文件,并可以直接通过jvm编译, 所以请不要使用代码块标记. \n" +
                    "\t输出规则2: \"单元测试代码\"尽可能覆盖所有分支场景\n" +
                    "\t输出规则3: \"单元测试代码\"默认规则: 类名称+方法名称+Test 如示例: DemoInfoServiceDoDetailTest; 如果方法是\"##other##\", 规则: 类名称+Test,如: DemoInfoServiceTest\n" +
                    "\t输出规则4: 要遵从java编译原理,不允许直接访问和修改类的私有属性。所有依赖项应通过依赖注入进行模拟和控制。如果必须要修改私有属性且没有找到public方法，可以将建议填写到单元测试类描述中.如:建议修改目标代码以提供适当的setter方法或构造函数注入，以便在单元测试中可以进行依赖注入和控制\n" +
                    "\t输出规则5: 生成的单元测试代码中请包含\"package信息\". 默认和类属于同一个包\n" +
                    "\t输出规则6: \"示例输出\"中setUp()方法中需要增加System.setProperty(\"spring.profiles.active\", \"local\");\n" +
                    "\t输出规则7: 禁止使用Arrays.asList等不可维护的集合.\n" +
                    "\t输出规则8: 所有@Autowired属性变量,都需要生成一个@Mock对象\n" +
                    "\t输出规则9: \"单元测试代码\"会保存到一个java maven工程Test source root下,文件保存目录:#{TEST_CASE_FOLDER}, 请根据文件保存目录调整\"单元测试代码\"中package包路径" ;



//    public static final String IDE_NAME = ApplicationInfo.getInstance().getVersionName();

    static {
        MODEL_MAP.put("GPT-3.5 Turbo", "gpt-3.5-turbo");
        MODEL_MAP.put("GPT-4", "gpt-4");
        MODEL_MAP.put("GPT-4V", "gpt-4v");
        MODEL_MAP.put("通义千问", "qwen");
        MODEL_MAP.put("文心一言", "wenxin");
        MODEL_MAP.put("ChatGLM", "chatglm");
        MODEL_MAP.put("Llama 3 70B", "tai-meta-llama/Llama-3-8b-chat-hf");

        // 配置ObjectMapper在反序列化时忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
