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
                <form:label path="type" cssClass="control-label">类型</form:label>
                <div class="controls">
                    <form:select id="userType" path="type"  cssClass="validate[required]" placeholder="请选择类型" >
                    	<form:options items="${bpmconfusertype}" itemValue="name" itemLabel="value"/>  
                    </form:select>
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="status" cssClass="control-label">数据来源</form:label>
                <div class="controls">
                	<form:select path="status"  cssClass="validate[required]" readonly="readonly">
                    	<form:options items="${bpmconfdatasource}" itemValue="name" itemLabel="value"/>  
                    </form:select>
                </div>
            </div>
            
            <div class="control-group">
                <form:label path="value" cssClass="control-label">用户/用户组</form:label>
                <div class="controls input-append">
                    <form:input id="showvalue" path="showValue" cssClass="validate[required]" readonly="true"/>
                    <span class="add-on"><i class="icon-chevron-down"></i></span>
                    <form:hidden id="value" path="value"/>
                </div>
            </div>
            
            <form:hidden path="priority"/>
            <!-- 
            <div class="control-group">
                <form:label path="priority" cssClass="control-label">排序</form:label>
                <div class="controls">
                    <form:input path="priority" cssClass="validate[required]" placeholder="请输入优先级"/>
                </div>
            </div>
            -->

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
            "url": "${ctx}/bpm/conf/user/validate",
            //动态提取的数据。验证时一起发送
            extraDataDynamic : ['#id', '#value', '#type', '#status', '#bpmConfNodeId'],
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
            
            updateUrl : "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/{id}/update?BackURL=" + $.table.tableURL($(".table")),
            deleteUrl : "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/{id}/delete",
            alwaysNew : "${param.copy}"
        });
        
        $("[name='value']").siblings(".add-on").andSelf().click(function() {

        	var userType = $("#userType").val();
        	
        	if("0"==userType){
        		$.app.modalDialog(
                        "用户列表",
                        "${ctx}/admin/sys/user/userselect/single;domId=value;domName=showvalue",
                        {
                            width:600,
                            height:450,
                            callback : function() {
                                $("[name='showvalue']").validationEngine('validate');
                                return true;
                            }
                        }
                 );
        	}
        	else if("1"==userType){
        		$.app.modalDialog(
                        "用户列表",
                        //"${ctx}/admin/sys/user/userselect/multiple;domId=value;domName=showvalue",
                        "${ctx}/admin/sys/user/userselect/single;domId=value;domName=showvalue",
                        {
                            width:600,
                            height:450,
                            callback : function() {
                                $("[name='showvalue']").validationEngine('validate');
                                return true;
                            }
                        }
                 );
        	}
        	else if("2"==userType){
        		$.app.modalDialog(
                        "用户组列表",
                        "${ctx}/admin/sys/group/groupselect/single;domId=value;domName=showvalue",
                        {
                            width:600,
                            height:450,
                            callback : function() {
                                $("[name='showvalue']").validationEngine('validate');
                                return true;
                            }
                        }
                 );
        	}
        	
        });
    });

</script>