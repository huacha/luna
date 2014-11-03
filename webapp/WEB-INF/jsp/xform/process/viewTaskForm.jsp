<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="row-fluid">
	<h3 align="center">${processName}</h3>
	<!-- start of main -->
    <section id="m-main" class="span10" style="float:center">
    	<div id="previousData" class="form-horizontal">
			<c:forEach items="${datas}" var="model">
				<fieldset>
					<legend><span class="span5">${model.taskName}</span> <span class="span7" style="font-size:9pt;text-align:right;font-weight:100; ">处理人：${model.assignee}&nbsp;&nbsp;&nbsp;处理时间：<fmt:formatDate value="${model.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></legend>
					<c:forEach items="${model.datas}" var="data">
						<div class="control-group form-horizontal">
							<label class="control-label">${data.title}</label>
							<div class="controls">
								<input type="text" value="${data.value}" readonly="true">
							</div>
						</div>
					</c:forEach>
				</fieldset>
			</c:forEach>
		</div>
		<br>
      <form id="xf-form" method="post" action="" class="xf-form">
      	<input type="hidden" name="taskId" value="${taskId}">
      	<input type="hidden" name="taskName" value="${taskName}">
      	<input type="hidden" name="formId" value="${formId}">
	      <fieldset>
	      	<legend>当前任务处理</legend>
	      	<div id="previousStep"></div> 
	      	<div id="nextStep"></div>
			<div id="xf-form-table"></div>
			<br>
			<div id="xf-form-button" style="text-align:center;">
		  </fieldset>
	  </form>

    </section>
	<!-- end of main -->

    <form:form id="f" commandName="m" method="post" style="display:none;">
	  <textarea id="__gef_content__" name="content">${m.content}</textarea>
	</form:form>
</div>

		
<es:contentFooter/>
<link href="${ctx}/widgets/xform/styles/xform.css" rel="stylesheet">
<script src="${ctx}/widgets/xform/xform-all.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/widgets/userpicker/userpicker.css">
<script type="text/javascript" src="${ctx}/widgets/userpicker/userpicker.js"></script>
<script type="text/javascript">
document.onmousedown = function(e) {};
document.onmousemove = function(e) {};
document.onmouseup = function(e) {};
document.ondblclick = function(e) {};

var buttons = [];
<c:forEach items="${buttons}" var="item">
buttons.push('${item}');
</c:forEach>

if (buttons.length == 0) {
	buttons = ['完成任务','驳回'];
}

var html = '';
for (var i = 0; i < buttons.length; i++) {
	html += '<button type="button">' + buttons[i] + '</button>';
}

var xform;

$(function() {
	
	function openDelegateForm(title, viewurl, posturl, backurl) {
        
        $.app.modalDialog(title, viewurl, {
            width:600,
            height:300,
            ok : function(modal) {
            	
				
                var form = modal.find("#editForm");
                if(!form.validationEngine('validate')) {
                    return false;
                }
                form.submit();
				/*
                $.post(posturl, form.serialize(), function(){
                	//return false;
                }
                
                );
				*/

                return true;
            }
        });
    }
    
	$('#xf-form-button').html(html);
	
	xform = new xf.Xform('xf-form-table');
	xform.render();

	if ($('#__gef_content__').val() != '') {
		xform.doImport($('#__gef_content__').val());
	} 
	
	if ('${json}' != '') {
		xform.setValue(${json});
	}
	
	$('.xf-cursor,.xf-handler').addClass('xf-cursor-auto');
	
	$(document).delegate('#xf-form-button button', 'click', function(e) {
		switch($(this).html()) {
			case '保存草稿':
				$('#xf-form').attr('action', 'saveDraft');
				$('#xf-form').submit();
				break;
			case '完成任务':
				$('#xf-form').attr('action', '${ctx}/xform/process/completeTask');
				$('#xf-form').submit();
				break;
			case '发起流程':
				$('#xf-form').attr('action', '');
				$('#xf-form').submit();
				break;
			case '驳回':
				$('#xf-form').attr('action', '${ctx}/xform/process/rollbackTask');
				$('#xf-form').submit();
				break;
			case '转办':
		        var viewurl = "${ctx}/xform/process/doDelegate?taskId=${taskId}";
                var posturl = "${ctx}/xform/process/doDelegate";
				openDelegateForm("转办", viewurl, posturl);
				break;
			case '协办':
		        var viewurl = "${ctx}/xform/process/doDelegateHelp?taskId=${taskId}";
                var posturl = "${ctx}/xform/process/doDelegateHelp";
				openDelegateForm("协办", viewurl, posturl);
				break;
		}
	});

	$.getJSON('${ctx}/xform/process/previous', {
		  processDefinitionId: '${processDefinitionId}',
		  activityId: '${activityId}'
	}, function(data) {
		if(data.length > 0){
			$('#previousStep').append('上个环节：');
		}
		for (var i = 0; i < data.length; i++) {
			if(i != 0){
				$('#previousStep').append(',');
			}
			$('#previousStep').append(data[i]);
		}
	});

	$.getJSON('${ctx}/xform/process/next', {
		  processDefinitionId: '${processDefinitionId}',
		  activityId: '${activityId}'
	}, function(data) {
		if(data.length > 0){
			$('#nextStep').append('下个环节：');
		}
		for (var i = 0; i < data.length; i++) {
			if(i != 0){
				$('#nextStep').append(',');
			}
			$('#nextStep').append(data[i]);
		}
	});
});
</script>
