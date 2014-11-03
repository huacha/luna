<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader />
<div data-table="table" class="sort-table table table-bordered table-hover" data-async="true">

	<es:showMessage />

	<div class="row-fluid tool ui-toolbar">
		<div class="span4">
			<a href="<es:BackURL/>"> <i class="icon-reply"></i> 返回
			</a>
		</div>
	</div>

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

	<br /> <br />

</div>
<es:contentFooter />
