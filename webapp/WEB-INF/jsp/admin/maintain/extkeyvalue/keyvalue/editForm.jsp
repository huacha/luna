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


    <form:form id="childEditForm" method="post" commandName="extKeyValue" cssClass="form-horizontal">
            <es:showGlobalError commandName="extKeyValue"/>
            <form:hidden path="id"/>

            <div class="control-group">
                <form:label path="name" cssClass="control-label">key</form:label>
                <div class="controls">
                    <form:input path="name" cssClass="validate[required, funcCall[$.parentchild.validateChildForm]]" placeholder="请输入key"/>
                </div>
            </div>

            <div class="control-group">
                <form:label path="value" cssClass="control-label">value</form:label>
                <div class="controls">
                    <form:input path="value" cssClass="validate[required]" placeholder="请输入value"/>
                </div>
            </div>

            <div class="control-group">
                <form:label path="show" cssClass="control-label">是否显示</form:label>
                <div class="controls inline-radio">
                    <form:radiobuttons path="show" items="${booleanList}" itemLabel="info" itemValue="value" cssClass="validate[required]"/>
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
    	
    	//自定义参数调用
        var validationEngine = $("#childEditForm").validationEngine("attach",{ 
        	tableId : "extKeyValueTable", 
        	trId : "${param.trId}"
        }); 

        $.app.initDatetimePicker();

        $.parentchild.initChildForm({
            form : $("#childEditForm"),
            tableId : "extKeyValueTable",
            excludeInputSelector : "[name='_show'],[name='_type']",
            trId : "${param.trId}",
            validationEngine : validationEngine,
            modalSettings:{
                width:600,
                height:420,
                noTitle : true,
                buttons:{}
            },
            updateUrl : "${ctx}/admin/maintain/extkeyvalue/category/keyvalue/{id}/update",
            deleteUrl : "${ctx}/admin/maintain/extkeyvalue/category/keyvalue/{id}/delete",
            alwaysNew : "${param.copy}"
        });
    });
    
</script>