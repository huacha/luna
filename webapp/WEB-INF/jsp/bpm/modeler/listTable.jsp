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
        <th style="width: 180px">
            <a class="check-all" href="javascript:;">全选</a>
            |
            <a class="reverse-all" href="javascript:;">反选</a>
        </th>
        <th style="width: 100px" sort="id">id</th>
        <th style="width: 150px" sort="name">name</th>
        <th>key</th>
        <th>version</th>
        <th>category</th>
        <th>createTime</th>
        <th>lastUpdateTime</th>
        <th>deploymentId</th>
        <th>metaInfo</th>
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
            <td>${m.key}</td>
            <td>${m.version}</td>
            <td>${m.category}</td>
            <td>${m.createTime}</td>
            <td>${m.lastUpdateTime}</td>
            <td>${m.deploymentId}</td>
            <td>${m.metaInfo}</td>
            
        </tr>
    </c:forEach>
    </tbody>
</table>
