<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<div class="panel">

    <ul class="nav nav-tabs">
        <c:if test="${op eq '新增'}">
            <li ${op eq '新增' ? 'class="active"' : ''}>
                <a>
                    <i class="icon-file-alt"></i>
                    新增
                </a>
            </li>
        </c:if>

        <c:if test="${op eq '复制'}">
            <li class="active">
                <a>
                    <i class="icon-copy"></i>
                    复制
                </a>
            </li>
        </c:if>
        <c:if test="${op eq '修改'}">
            <li class="active">
                <a>
                    <i class="icon-edit"></i>
                    修改
                </a>
            </li>
        </c:if>
        <li>
            <a href="javascript:$.app.cancelModelDialog();" class="btn btn-link">
                <i class="icon-remove"></i>
                取消
            </a>
        </li>
    </ul>

    <form:form id="childEditForm" method="post" commandName="m" cssClass="form-horizontal">
            <es:showGlobalError commandName="m"/>
            <form:hidden path="id"/>
            <form:hidden id="bpmConfNodeId" path="bpmConfNode.id"/>

            <div class="control-group">
                <form:label path="value" cssClass="control-label">规则名称</form:label>
                <div class="controls">
                    <form:input path="value" cssClass="validate[required, ajax[ajaxNameCall]]" placeholder="请输入规则名称"/>
                </div>
            </div>
            
            <c:if test="${op eq '新增'}">
                <c:set var="icon" value="icon-file-alt"/>
            </c:if>
            <c:if test="${op eq '修改'}">
                <c:set var="icon" value="icon-edit"/>
            </c:if>
            <c:if test="${op eq '复制'}">
                <c:set var="icon" value="icon-copy"/>
            </c:if>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary">
                        <i class="${icon}"></i>
                            ${op}
                    </button>
                    <a href="javascript:$.app.cancelModelDialog();" class="btn">
                        <i class="icon-remove"></i>
                        取消
                    </a>
                </div>
            </div>

    </form:form>
</div>
<script type="text/javascript">
    $(function () {

        //自定义ajax验证  ajax[ajaxNameCall] 放到验证规则的最后（放到中间只有当submit时才验证）
        $.validationEngineLanguage.allRules.ajaxNameCall= {
            "url": "${ctx}/bpm/conf/rule/validate",
            //动态提取的数据。验证时一起发送
            extraDataDynamic : ['#id', '#bpmConfNodeId'],
            //验证失败时的消息
            //"alertText": "* 该名称已被其他人使用",
            //验证成功时的消息
            //"alertTextOk": "该名称可以使用",
            "alertTextLoad": "* 正在验证，请稍等。。。"
        };
        
        var validationEngine = $("#childEditForm").validationEngine(); 

        $.app.initDatetimePicker();

        $.noparentchild.initChildForm({
            form : $("#childEditForm"),
            tableId : "childTable",
            excludeInputSelector : "[name='_show'],[name='_type']",
            trId : "${param.trId}",
            validationEngine : validationEngine,
            modalSettings:{
                width:600,
                height:450,
                noTitle : false,
                buttons:{}
            },
            
            updateUrl : "${ctx}/bpm/conf/rule/node-${bpmConfNode.id}/{id}/update?BackURL=" + $.table.tableURL($(".table")),
            deleteUrl : "${ctx}/bpm/conf/rule/node-${bpmConfNode.id}/{id}/delete",
            alwaysNew : "${param.copy}"
        });
    });

</script>