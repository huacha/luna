<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="panel">

    <ul class="nav nav-tabs">
        <li>
            <a href="<es:BackURL/>" class="btn btn-link">
                <i class="icon-reply"></i>
                返回
            </a>
        </li>
    </ul>

    <form:form id="editForm" method="post" commandName="m" cssClass="form-horizontal">
        <!--上一个地址 如果提交方式是get 需要加上-->
        <%--<es:BackURL hiddenInput="true"/>--%>

            <es:showGlobalError commandName="m"/>
			
			<!--该id为流程模型id-->
            <!-- <input type="hidden" id="modelId" name="modelId" value="${modelId}" }/> -->
            
            <!--该id为bpmProcessId-->
            <form:hidden path="id"/>

            <div class="control-group">
                <form:label path="name" cssClass="control-label">流程名称</form:label>
                <div class="controls">
                    <form:input path="name" cssClass="validate[required, ajax[ajaxNameCall]]" placeholder="请输入流程名称"/>
                </div>
            </div>

            <div class="control-group">
                <form:label path="bpmCategory.name" cssClass="control-label">所属类别</form:label>
                <div class="controls input-append">
                    <form:input id="categoryName" path="bpmCategory.name" cssClass="validate[required]" readonly="true"/>
                    <span class="add-on"><i class="icon-chevron-down"></i></span>
                    <form:hidden id="categoryId" path="bpmCategory.id"/>
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="priority" cssClass="control-label">排序</form:label>
                <div class="controls">
                    <form:input path="priority"  readonly="true" value="50"/>
                </div>
            </div>

            <div class="control-group">
                <form:label path="useTaskConf" cssClass="control-label">配置任务负责人</form:label>
                <div class="controls  inline-radio">
                    <label><input id="useTaskConf_0" type="radio" name="useTaskConf" value="1" ${model.useTaskConf == 1 ? 'checked' : ''}>开启</label>
                    <label><input id="useTaskConf_1" type="radio" name="useTaskConf" value="0" ${model.useTaskConf != 1 ? 'checked' : ''}>关闭</label>
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="descn" cssClass="control-label">描述</form:label>
                <div class="controls">
                    <form:textarea path="descn" style="width: 400px" placeholder="描述"/>
                </div>
            </div>

            <c:set var="icon" value="icon-file-alt"/>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn">
                        <i class="${icon}"></i>
                            发布
                    </button>
                    <a href="<es:BackURL/>" class="btn">
                        <i class="icon-reply"></i>
                        返回
                    </a>
                </div>
            </div>
    </form:form>
</div>
<es:contentFooter/>
<script type="text/javascript">
    $(function () {
        //自定义ajax验证  ajax[ajaxNameCall] 放到验证规则的最后（放到中间只有当submit时才验证）
        $.validationEngineLanguage.allRules.ajaxNameCall= {
            "url": "${ctx}/bpm/process/process/validate",
            //动态提取的数据。验证时一起发送
            extraDataDynamic : ['#id'],
            //验证失败时的消息
            //"alertText": "* 该名称已被其他人使用",
            //验证成功时的消息
            //"alertTextOk": "该名称可以使用",
            "alertTextLoad": "* 正在验证，请稍等。。。"
        };
        var validationEngine = $("#editForm").validationEngine();
        <es:showFieldError commandName="m"/>
        
        $("[name='bpmCategory.name']").siblings(".add-on").andSelf().click(function() {
            $.app.modalDialog(
                    "类别列表",
                    "${ctx}/bpm/category/select/single;domId=categoryId;domName=categoryName",
                    {
                        width:600,
                        height:450,
                        callback : function() {
                            $("[name='bpmCategory.name']").validationEngine('validate');
                            return true;
                        }
                    }
             );
        });
        
    });
</script>