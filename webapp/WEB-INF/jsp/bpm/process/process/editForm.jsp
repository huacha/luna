<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="panel">

    <ul class="nav nav-tabs">
        <shiro:hasPermission name="bpm:process:process:create">
        <c:if test="${op eq '新增'}">
            <li ${op eq '新增' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/process/create?BackURL=<es:BackURL/>">
                    <i class="icon-file-alt"></i>
                    新增
                </a>
            </li>
        </c:if>
        </shiro:hasPermission>


        <c:if test="${not empty m.id}">
            <li ${op eq '查看' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/process/${m.id}?BackURL=<es:BackURL/>">
                    <i class="icon-eye-open"></i>
                    查看
                </a>
            </li>
            <shiro:hasPermission name="bpm:process:process:update">
            <li ${op eq '修改' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/process/${m.id}/update?BackURL=<es:BackURL/>">
                    <i class="icon-edit"></i>
                    修改
                </a>
            </li>
            </shiro:hasPermission>

            <shiro:hasPermission name="bpm:process:process:delete">
            <li ${op eq '删除' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/process/${m.id}/delete?BackURL=<es:BackURL/>">
                    <i class="icon-trash"></i>
                    删除
                </a>
            </li>
            </shiro:hasPermission>
        </c:if>
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
                <form:label path="bpmConfBase.processDefinitionId" cssClass="control-label">绑定流程</form:label>
                <div class="controls input-append">
                    <form:input id="processDefinitionId" path="bpmConfBase.processDefinitionId" cssClass="validate[required, ajax[ajaxNameCall]]" readonly="true"/>
                    <span class="add-on"><i class="icon-chevron-down"></i></span>
                    <form:hidden id="confBaseId" path="bpmConfBase.id"/>
                </div>
                <!--  
                <div class="controls">
                    <form:input path="code" cssClass="validate[required]" placeholder="请选择绑定流程"/>
                </div>
                -->
            </div>
            
            <div class="control-group">
                <form:label path="priority" cssClass="control-label">排序</form:label>
                <div class="controls">
                    <form:input path="priority" cssClass="validate[required]" placeholder="请输入排序"/>
                </div>
            </div>

            <div class="control-group">
                <form:label path="useTaskConf" cssClass="control-label">配置任务负责人</form:label>
                <div class="controls  inline-radio">
                
                    <label><input id="useTaskConf_0" type="radio" name="useTaskConf" value="1" ${model.useTaskConf == 1 ? 'checked' : ''}>开启</label>
                    <label><input id="useTaskConf_1" type="radio" name="useTaskConf" value="0" ${model.useTaskConf != 1 ? 'checked' : ''}>关闭</label>
    
                    <!-- form:radiobuttons path="useTaskConf" items="${booleanList}" itemLabel="info" itemValue="value" cssClass="validate[required]"/ -->
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="descn" cssClass="control-label">描述</form:label>
                <div class="controls">
                    <form:textarea path="descn" style="width: 400px" placeholder="描述"/>
                </div>
            </div>

            <c:if test="${op eq '新增'}">
                <c:set var="icon" value="icon-file-alt"/>
            </c:if>
            <c:if test="${op eq '修改'}">
                <c:set var="icon" value="icon-edit"/>
            </c:if>
            <c:if test="${op eq '删除'}">
                <c:set var="icon" value="icon-trash"/>
            </c:if>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary">
                        <i class="${icon}"></i>
                            ${op}
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
        <c:choose>
            <c:when test="${op eq '删除'}">
                //删除时不验证 并把表单readonly
                $.app.readonlyForm($("#editForm"), false);
            </c:when>
            <c:when test="${op eq '查看'}">
                $.app.readonlyForm($("#editForm"), true);
            </c:when>
            <c:otherwise>
                //自定义ajax验证  ajax[ajaxNameCall] 放到验证规则的最后（放到中间只有当submit时才验证）
                $.validationEngineLanguage.allRules.ajaxNameCall= {
                    "url": "${ctx}/bpm/process/process/validate",
                    //动态提取的数据。验证时一起发送
                    extraDataDynamic : ['#id'],
                    //验证失败时的消息
                    //"alertText": "* 该名称已被其他人使用",
                    //验证成功时的消息
                    //"alertTextOk": "该名称可以使用",
                    "alertTextLoad": "* 正在验证111，请稍等。。。"
                };
                $.validationEngineLanguage.allRules.username={
                    "regex": /^\w{5,10}$/,
                    "alertText": "* 5到10个字母、数字、下划线"
                };
                var validationEngine = $("#editForm").validationEngine();
                <es:showFieldError commandName="m"/>
            </c:otherwise>
        </c:choose>
        
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
        
        $("[name='bpmConfBase.processDefinitionId']").siblings(".add-on").andSelf().click(function() {
            $.app.modalDialog(
                    "流程列表",
                    "${ctx}/bpm/conf/base/select/single;domId=confBaseId;domName=processDefinitionId",
                    {
                        width:600,
                        height:450,
                        callback : function() {
                            $("[name='bpmConfBase.processDefinitionKey']").validationEngine('validate');
                            return true;
                        }
                    }
             );
        });
        
    });
</script>