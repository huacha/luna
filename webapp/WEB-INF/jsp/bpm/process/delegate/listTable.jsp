<%--
    <div data-table="table">
       <a class='btn-create'>  新增
       <a class='btn-update'>  修改
       <a class='btn-delete'> 删除

       <a class='btn-custom'> 自定义按钮 系统不处理

    异步表格：
    table
        data-async="true"
        data-async-container 设定异步内容加载到的容器
        data-async-callback  设定异步内容加载完成后执行的回调 默认到当前window里边去找同名的函数，自动注入当前table
        data-url 异步加载时使用的url
        data-prefix 指定分页/排序的字段前缀
    search form (class='search-form')
        data-search-table 指定search table的id
        data-change-search 指定是否表单里的输入框 改变时触发查询

        class="btn-search-all" 表示查询所有
    </div>
--%>
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
        <th style="width: 150px" sort="startTime">开始时间</th>
        <th style="width: 150px"  sort="endTime">结束时间</th>
        <th style="width: 180px" sort="processDefinitionId">流程定义</th>
        <th style="width: 80px" sort="status">状态</th>
        <th ></th>
        
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${page.content}" var="m">
        <tr>
            <td class="check"><input type="checkbox" name="ids" value="${m.id}"></td>
            <td>
                <a class="btn btn-link btn-edit" href="${ctx}/bpm/process/delegate/${m.id}">${m.id}</a>
            </td>
            <td>${m.assignee}</td>
            <td>${m.attorney}</td>
            <td>${m.startTime}</td>
            <td>${m.endTime}</td>
            <td>${m.processDefinitionId}</td>
            <td>${m.status}</td>
            <td></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<es:page page="${page}"/>
