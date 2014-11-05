<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<table id="table" class="sort-table table table-bordered table-hover" data-async="false">
	<thead>
		<tr>
			<th style="width: 70px">
			    <a class="check-all" href="javascript:;">全选</a>
				| <a class="reverse-all" href="javascript:;">反选</a>
			</th>
			<th style="width: 80px" >任务编号</th>
			<th style="width: 150px" >流程名称</th>
			<th style="width: 150px" >任务名称</th>
			
			<c:if test="${param['taskstatus'] ne 'finished'}">
				<th style="width: 150px" >创建时间</th>
				<th style="width: 60px" >负责人</th>
				<th style="width: 40px" >状态</th>
			</c:if>
			
			
			<c:if test="${param['taskstatus'] eq 'finished'}">
				<th style="width: 150px" >开始时间</th>
				<th style="width: 150px" >结束时间</th>
				<th style="width: 60px" >负责人</th>
				<th style="width: 60px" >处理结果</th>
			</c:if>
			
			<th style="width: 130px">操作</th>
			<th></th>
			
		</tr>
	</thead>
	<tbody>

		<c:forEach items="${page.content}" var="m">
			<tr>
				<td class="check"><input type="checkbox" name="ids"	value="${m.id}"></td>
				<td>${m.id}</td>
				<td><!-- ${bpmProcessMap['${m.processDefinitionId}'].name} -->
				<c:forEach items="${bpmProcessMap}" var="map">
				  <c:if test="${map.key eq m.processDefinitionId}">
				    ${map.value.name}
				  </c:if>  
			    </c:forEach>
				</td>
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
					    &nbsp;
						<a href="${ctx}/bpm/usertask/unclaim?taskId=${m.id}&taskstatus=prepare">释放任务</a>
						&nbsp;
		            </c:if>
					<c:if test="${param['taskstatus'] eq 'preclaim'}">
						<a href="${ctx}/bpm/usertask/claim?taskId=${m.id}&taskstatus=preclaim">领取</a>
					    &nbsp;
		            </c:if>
					<c:if test="${param['taskstatus'] eq 'claimed'}">
					    <a href="${ctx}/xform/process/viewTaskForm?taskId=${m.id}&taskstatus=claimed">代理处理</a>
					    &nbsp;
		            </c:if>
					<c:if test="${param['taskstatus'] eq 'finished'}">
						<a href="${ctx}/bpm/usertask/revoke?taskId=${m.id}&taskstatus=finished">撤销</a>
					    &nbsp;
		            </c:if>
		            <a href="${ctx}/bpm/process/workspace/viewHistory?processInstanceId=${m.processInstanceId}">历史</a>
				</td>
				<td></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<es:page page="${page}" />
