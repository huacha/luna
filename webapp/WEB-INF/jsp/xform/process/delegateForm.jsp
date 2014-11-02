<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<div class="panel">


    <form:form id="editForm" method="post" commandName="m" cssClass="form-horizontal">
            <es:showGlobalError commandName="m"/>
            <!-- <input type="hidden" id="taskId" name="taskId" value="${taskId}" /> -->
            <div class="control-group">
                <form:label path="username" cssClass="control-label">代理人</form:label>
                <div class="controls input-append">
                    <form:input id="username" path="username" cssClass="validate[required]" readonly="true"/>
                    <span class="add-on"><i class="icon-chevron-down"></i></span>
                    <form:hidden id="id" path="id"/>
                </div>
            </div>

    </form:form>
</div>
<script type="text/javascript">
    $(function () {

        $("[name='username']").siblings(".add-on").andSelf().click(function() {
       		$.app.modalDialog(
                 "用户列表",
                 "${ctx}/admin/sys/user/usernameselect/single;domId=id;domName=username",
                 {
                     width:600,
                     height:450,
                     callback : function() {
                         $("[name='username']").validationEngine('validate');
                         return true;
                     }
                 }
        	);
        });
    });

</script>