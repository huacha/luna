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
			<th style="width: 150px" >流程名称</th>
			<th style="width: 150px" >创建时间</th>
			<th style="width: 150px" >结束时间</th>
			<th style="width: 120px" >发起人</th>
			<th style="width: 120px">操作</th>
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
				
				<td><fmt:formatDate value="${m.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${m.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${m.startUserId}</td>
				
				<td>
					<c:if test="${param['processstatus'] eq 'unfinished'}">
					    <a href="${ctx}/bpm/userprocess/${m.id}/terminate">终止</a>
		            &nbsp;&nbsp;
		            </c:if>
					<a href="${ctx}/bpm/process/workspace/viewHistory?processInstanceId=${m.processInstanceId}">历史</a>
				</td>
				<td></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<es:page page="${page}" />
