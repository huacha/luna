<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader />
<div data-table="table" class="sort-table table table-bordered table-hover" data-async="false">

	<es:showMessage />

	<div class="row-fluid tool ui-toolbar">
		<div class="span4">
			<a href="<es:BackURL/>"> <i class="icon-reply"></i> 返回
			</a>
		</div>
	</div>
	<table id="nodeTable" class="table table-bordered table-hover" >
		<h4 class="title">流程信息</h4>
		<tr>
			<td style="width: 100px; text-align:right" >流程名称:</td>
			<td style="width: 250px">${bpmProcess.name} </td>
			<td style="width: 100px; text-align:right" align="right">发起人：</td>
			<td style="width: 250px">${historicProcess.startUserId} </td>
		</tr>
		<tr>
			<td style="text-align:right">发起时间:</td>
			<td ><fmt:formatDate value="${historicProcess.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td style="text-align:right">结束时间:</td>
			<td ><fmt:formatDate value="${historicProcess.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
	</table>

	<article class="m-widget">
		<header class="header">
			<h4 class="title">流程图</h4>
		</header>
		<div id="demoSearch" class="content">
			<img src="${ctx}/bpm/process/workspace/graphHistoryProcessInstance?processInstanceId=${param.processInstanceId}">
		</div>
	</article>

	<table id="tasklisttable" class="sort-table table table-bordered table-hover" data-async="true">
		
		<h4 class="title">流程任务列表</h4>
		<thead>
			<tr>
				<th style="width: 80px" >编号</th>
				<th style="width: 150px" >名称</th>
				<th style="width: 80px" >开始时间</th>
				<th style="width: 80px" >结束时间</th>
				<th style="width: 80px" >负责人</th>
				<th style="width: 80px" >处理结果</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${historicTasks}" var="item">
				<tr>
					<td>${item.id}</td>
					<td>${item.name}</td>
					<td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${item.assignee} </td>
					<td>${item.deleteReason}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div id="previousData" class="form-horizontal">
	<h4 class="title">流程表单数据</h4>
		<c:forEach items="${datas}" var="model">
			<fieldset>
				<legend>
					<span class="span5">${model.taskName}</span> 
					<span class="span7" style="font-size: 9pt; text-align: right; font-weight: 100;">
						处理人：${model.assignee}&nbsp;&nbsp;&nbsp;处理时间：
						<fmt:formatDate value="${model.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
					</span>
				</legend>
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
	<br /> <br />

</div>
<es:contentFooter />
