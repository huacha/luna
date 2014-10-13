<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="panel">

    <ul class="nav nav-tabs">
        <shiro:hasPermission name="bpm:process:delegate:create">
        <c:if test="${op eq '新增'}">
            <li ${op eq '新增' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/delegate/create?BackURL=<es:BackURL/>">
                    <i class="icon-file-alt"></i>
                    新增
                </a>
            </li>
        </c:if>
        </shiro:hasPermission>
        <c:if test="${not empty m.id}">
            <li ${op eq '查看' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/delegate/${m.id}?BackURL=<es:BackURL/>">
                    <i class="icon-eye-open"></i>
                    查看
                </a>
            </li>
            <shiro:hasPermission name="bpm:process:delegate:update">
            <li ${op eq '修改' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/delegate/${m.id}/update?BackURL=<es:BackURL/>">
                    <i class="icon-edit"></i>
                    修改
                </a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="bpm:process:delegate:delete">
            <li ${op eq '删除' ? 'class="active"' : ''}>
                <a href="${ctx}/bpm/process/delegate/${m.id}/delete?BackURL=<es:BackURL/>">
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
                <form:label path="assignee" cssClass="control-label">委托人</form:label>
                <div class="controls">
                    <form:input id="assigneeName" path="assigneeName" cssClass="validate[required]" readonly="true"/>
                    <form:hidden id="assignee" path="assignee"/>
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="attorneyType" cssClass="control-label">代理人类型</form:label>
                <div class="controls inline-radio">
                    <!-- 
                    <form:radiobutton path="attorneyType" value="1" cssClass="validate[required]"/>单用户  
                    <form:radiobutton path="attorneyType" value="2" cssClass="validate[required]"/>用户组
                     -->
                    <form:radiobuttons path="attorneyType" items="${attorneyTypeList}" delimiter="&nbsp;&nbsp;" cssClass="validate[required]"/>
                </div>
            </div>
            

            <div class="control-group">
                <form:label path="attorney" cssClass="control-label">代理人</form:label>
                <div class="controls input-append">
                    <form:input id="attorneyName" path="attorneyName" cssClass="validate[required]" readonly="true"/>
                    <span class="add-on"><i class="icon-chevron-down"></i></span>
                    <form:hidden id="attorney" path="attorney"/>
                </div>
            </div>

            <div class="control-group">
                <form:label path="startTime" cssClass="control-label">开始时间</form:label>
                <div class="controls input-append date">
                    <form:input path="startTime" data-format="yyyy-MM-dd" placeholder="例如2013-02-07"/>
                    <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
                </div>
            </div>
            <div class="control-group">
                <form:label path="endTime" cssClass="control-label">开始时间</form:label>
                <div class="controls input-append date">
                    <form:input path="endTime" data-format="yyyy-MM-dd" placeholder="例如2013-02-07"/>
                    <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="processDefinitionId" cssClass="control-label">流程定义</form:label>
                <div class="controls input-append">
                    <form:input id="processDefinitionId" path="processDefinitionId" cssClass="validate[required]" readonly="true"/>
                    <span class="add-on"><i class="icon-chevron-down"></i></span>
                </div>
            </div>
            
            <form:hidden path="status"/>
            
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
                    "url": "${ctx}/bpm/process/delegate/validate",
                    //动态提取的数据。验证时一起发送
                    extraDataDynamic : ['#id'],
                    //验证失败时的消息
                    //"alertText": "* 该名称已被其他人使用",
                    //验证成功时的消息
                    //"alertTextOk": "该名称可以使用",
                    "alertTextLoad": "* 正在验证，请稍等。。。"
                };
                $.validationEngineLanguage.allRules.username={
                    "regex": /^\w{5,10}$/,
                    "alertText": "* 5到10个字母、数字、下划线"
                };
                var validationEngine = $("#editForm").validationEngine();
                <es:showFieldError commandName="m"/>
            </c:otherwise>
        </c:choose>
        
        $("[name='processDefinitionId']").siblings(".add-on").andSelf().click(function() {
            $.app.modalDialog(
                    "流程列表",
                    "${ctx}/bpm/conf/base/select/single;domId=processDefinitionId;domName=processDefinitionId",
                    {
                        width:600,
                        height:450,
                        callback : function() {
                            $("[name='processDefinitionId']").validationEngine('validate');
                            return true;
                        }
                    }
             );
        });
        
        $("[name='attorneyName']").siblings(".add-on").andSelf().click(function() {
        	var attorneyType = $("input[name='attorneyType']:checked").val();
        	
        	if("1"==attorneyType){
        		$.app.modalDialog(
                        "用户列表",
                        "${ctx}/bpm/process/delegate/userselect/single;domId=attorney;domName=attorneyName",
                        {
                            width:600,
                            height:450,
                            callback : function() {
                                $("[name='attorneyName']").validationEngine('validate');
                                return true;
                            }
                        }
                 );
        	}
        	else if("2"==attorneyType){
        		$.app.modalDialog(
                        "用户组列表",
                        "${ctx}/bpm/process/delegate/groupselect/single;domId=attorney;domName=attorneyName",
                        {
                            width:600,
                            height:450,
                            callback : function() {
                                $("[name='attorneyName']").validationEngine('validate');
                                return true;
                            }
                        }
                 );
        	}
        });
        
    });
</script>