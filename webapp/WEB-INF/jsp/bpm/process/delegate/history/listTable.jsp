<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<table id="table" class="sort-table table table-bordered table-hover" data-async="true">
    <thead>
    <tr>
        <th style="width: 70px">
            <a class="check-all" href="javascript:;">全选</a>
            |
            <a class="reverse-all" href="javascript:;">反选</a>
        </th>
        <th style="width: 80px" sort="id">编号</th>
        <th style="width: 150px" sort="assignee">委托人</th>
        <th style="width: 150px" sort="attorney">被委托人</th>
        <th style="width: 150px" sort="delegateTime">委托时间</th>
        <th style="width: 120px"  sort="taskId">任务ID</th>
        <th style="width: 80px" sort="status">状态</th>
        <th ></th>
        
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${page.content}" var="m">
        <tr>
            <td class="check"><input type="checkbox" name="ids" value="${m.id}"></td>
            <td>${m.id}</td>
            <td>${m.assignee}</td>
            <td>${m.attorney}</td>
            <td>${m.delegateTime}</td>
            <td>${m.taskId}</td>
            <td>${m.status}</td>
            <td>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<es:page page="${page}"/>
