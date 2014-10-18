<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="row-fluid">
	<section id="m-previous" class="span10" >
		<div id="previousData"></div>
	</section>
	
	<!-- start of main -->
    <section id="m-main" class="span10" style="float:center">
		<div id="previousStep"></div>
		<script>
		  $.getJSON('${scopePrefix}/rs/bpm/previous', {
			  processDefinitionId: '${formInfo.processDefinitionId}',
			  activityId: '${formInfo.activityId}'
		  }, function(data) {
			  $('#previousStep').append('上个环节：');
			  for (var i = 0; i < data.length; i++) {
				  $('#previousStep').append(data[i].name);
			  }
		  });
		 </script>
		  
		<div id="nextStep"></div>
		<script>
		  $.getJSON('${scopePrefix}/rs/bpm/next', {
			  processDefinitionId: '${formInfo.processDefinitionId}',
			  activityId: '${formInfo.activityId}'
		  }, function(data) {
			  $('#nextStep').append('下个环节：');
			  for (var i = 0; i < data.length; i++) {
				  $('#nextStep').append(data[i].name);
			  }
		  });
		 </script>
		  
      <form id="xf-form" method="post" action="" class="xf-form">
		<div id="xf-form-table"></div>
		<br>
		
		<div style="text-align:center;">
		  <button id="button0" type="button">发起流程</button>
		</div>
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
	buttons = ['保存草稿', '完成任务'];
}

var html = '';
for (var i = 0; i < buttons.length; i++) {
	html += '<button type="button">' + buttons[i] + '</button>';
}

var xform;

$(function() {
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
				$('#xf-form').attr('action', 'form-saveDraft.do');
				$('#xf-form').submit();
				break;
			case '完成任务':
				$('#xf-form').attr('action', 'form-completeTask.do');
				$('#xf-form').submit();
				break;
			case '发起流程':
				$('#xf-form').attr('action', 'form-startProcessInstance.do');
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

});
</script>
