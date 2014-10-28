<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader />
<div data-table="table" class="panel">

	<es:showMessage />

	<div class="row-fluid tool ui-toolbar">
		<div class="span4">
			<div class="btn-group">
				<a class="btn btn-create"> <i class="icon-file-alt"></i> 新增</a>
			</div>
		</div>
		<div class="span8"></div>
	</div>

	<table id="table" class="sort-table table table-bordered table-hover" data-async="true">
		<thead>
			<tr>
				<th style="width: 70px">
					<a class="check-all" href="javascript:;">全选</a> | <a class="reverse-all" href="javascript:;">反选</a>
				</th>
				<th style="width: 70px">编号</th>
				<th style="width: 100px">名称</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>部署号</th>
				<th>描述</th>
				<th style="width: 30px">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${models}" var="m">
				<tr>
					<td class="check"><input type="checkbox" name="ids" value="${m.id}"></td>
					<td>
						<a class="btn btn-link btn-edit" href="${ctx}/modeler/create?id=${m.id}">${m.id}</a>
					</td>
					<td>${m.name}</td>
					<td><fmt:formatDate value="${m.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td><fmt:formatDate value="${m.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td>${m.deploymentId}</td>
					<td>${m.metaInfo}</td>
					<td>
						<a class='btn-custom' href="${ctx}/modeler/remove?id=${m.id}">删除</a> 
						<a class='btn-custom' href="${ctx}/modeler/deploy?id=${m.id}">发布</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>

<es:contentFooter />