<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<table id="table" class="sort-table table table-bordered table-hover"
	data-async="true">
	<thead>
		<tr>
			<th style="width: 70px">
			    <a class="check-all" href="javascript:;">全选</a>
				| <a class="reverse-all" href="javascript:;">反选</a>
			</th>
			<th style="width: 80px" >编号</th>
			<th style="width: 150px" >名称</th>
			
			<c:if test="${param['taskstatus'] ne 'finished'}">
				<th style="width: 150px" >创建时间</th>
				<th style="width: 120px" >负责人</th>
				<th style="width: 80px" >状态</th>
			</c:if>
			
			
			<c:if test="${param['taskstatus'] eq 'finished'}">
				<th style="width: 150px" >开始时间</th>
				<th style="width: 150px" >结束时间</th>
				<th style="width: 120px" >负责人</th>
				<th style="width: 80px" >处理结果</th>
			</c:if>
			
			<th style="width: 120px">操作</th>
			<th></th>
			
		</tr>
	</thead>
	<tbody>

		<c:forEach items="${page.content}" var="m">
			<tr>
				<td class="check"><input type="checkbox" name="ids"	value="${m.id}"></td>
				<td>${m.id}</td>
				<td>${m.name}</td>
				
				<c:if test="${param['taskstatus'] ne 'finished'}">
					<td><fmt:formatDate value="${m.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${m.assignee}</td>
					<td>${m.suspended ? '挂起' : '激活'}</td>
                </c:if>
				
				<c:if test="${param['taskstatus'] eq 'finished'}">
					<td><fmt:formatDate value="${m.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${m.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${m.assignee}</td>
					<td>${m.deleteReason}</td>
	            </c:if>
				
				<td>
					<c:if test="${param['taskstatus'] eq 'prepare'}">
					    <a href="${ctx}/xform/process/viewTaskForm?taskId=${m.id}&taskstatus=prepare">处理</a>
					    <a href="#">历史</a>
		            </c:if>
					<c:if test="${param['taskstatus'] eq 'preclaim'}">
					    <a href="${ctx}/xform/process/viewTaskForm?taskId=${m.id}&taskstatus=preclaim">领取</a>
					    <a href="#">历史</a>
		            </c:if>
					<c:if test="${param['taskstatus'] eq 'claimed'}">
					    <a href="${ctx}/xform/process/viewTaskForm?taskId=${m.id}&taskstatus=claimed">代理处理</a>
					    <a href="#">历史</a>
		            </c:if>
					<c:if test="${param['taskstatus'] eq 'finished'}">
					    <a href="${ctx}/xform/process/viewTaskForm?taskId=${m.id}&taskstatus=finished">撤销</a>
					    <a href="#">历史</a>
		            </c:if>
				</td>
				<td></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<es:page page="${page}" />
