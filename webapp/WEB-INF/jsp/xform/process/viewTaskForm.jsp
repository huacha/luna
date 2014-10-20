<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="row-fluid">
	
	<!-- start of main -->
    <section id="m-main" class="span10" style="float:center">
    	<div id="previousData" class="form-horizontal">
			${data}
		</div>
		
		<br>
		
		<div id="previousStep"></div> <div id="nextStep"></div>
		  
      <form id="xf-form" method="post" action="" class="xf-form">
      	<input type="hidden" name="taskId" value="${taskId}">
      	<input type="hidden" name="formId" value="${formId}">
	      <fieldset>
	      	<legend>流程处理</legend>
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
	buttons = ['完成任务'];
}

var html = '';
for (var i = 0; i < buttons.length; i++) {
	html += '<button type="button">' + buttons[i] + '</button>';
}

var xform;

$(function() {
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
				$('#xf-form').attr('action', '${ctx}/bpm/workspace-rollback.do');
				$('#xf-form').submit();
				break;
			case '转办':
				$('#modal form').attr('action', '${ctx}/bpm/workspace-doDelegate.do');
				$('#modal').modal();
				break;
			case '协办':
				$('#modal form').attr('action', '${ctx}/bpm/workspace-doDelegateHelp.do');
				$('#modal').modal();
				break;
		}
	});
/*
	$.getJSON('${ctx}/rs/bpm/previous', {
		  processDefinitionId: '${formInfo.processDefinitionId}',
		  activityId: '${formInfo.activityId}'
	  }, function(data) {
		  $('#previousStep').append('上个环节：');
		  for (var i = 0; i < data.length; i++) {
			  $('#previousStep').append(data[i].name);
		  }
	  });
	
	  $.getJSON('${ctx}/rs/bpm/next', {
		  processDefinitionId: '${formInfo.processDefinitionId}',
		  activityId: '${formInfo.activityId}'
	  }, function(data) {
		  $('#nextStep').append('下个环节：');
		  for (var i = 0; i < data.length; i++) {
			  $('#nextStep').append(data[i].name);
		  }
	  });
	  */
});
</script>
