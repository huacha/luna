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

    
<h4 class="hr">流程节点配置</h4>

<table id="table" class="sort-table table table-bordered table-hover" data-async="true">
    <thead>
    <tr>
          <th>编号</th>
          <th>类型</th>
          <th>节点</th>
          <th>人员</th>
          <th>事件</th>
          <th>规则</th>
          <th>表单</th>
          <th>操作</th>
          <th>提醒</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${page.content}" var="item">
        <tr>
        
          <td>${item.id}</td>
		  <td>${item.type}</td>
          <td>${item.name}</td>
          <td>
		    <c:if test="${item.confUser == 0}">
			  <a href="${ctx}/bpm/conf/user/process-${bpmProcess.id}/node-${item.id}" class="btn"><i class="icon-edit"></i></a>
			</c:if>
		    <c:if test="${item.confUser == 1}">
			  <a href="${ctx}/bpm/conf/user/process-${bpmProcess.id}/node-${item.id}" class="btn btn-primary"><i class="icon-edit"></i></a>
			</c:if>
			&nbsp;
	      </td>
          <td>
		    <c:if test="${item.confListener == 0}">
			  <a href="${ctx}/bpm/conf/listener/process-${bpmProcess.id}/node-${item.id}" class="btn"><i class="icon-edit"></i></a>
			</c:if>
		    <c:if test="${item.confListener == 1}">
			  <a href="${ctx}/bpm/conf/listener/process-${bpmProcess.id}/node-${item.id}" class="btn btn-primary"><i class="icon-edit"></i></a>
			</c:if>
			&nbsp;
	      </td>
          <td>
		    <c:if test="${item.confRule == 0}">
			  <a href="bpm-conf-rule-list.do?bpmConfNodeId=${item.id}" class="btn"><i class="icon-edit"></i></a>
			</c:if>
		    <c:if test="${item.confRule == 1}">
			  <a href="bpm-conf-rule-list.do?bpmConfNodeId=${item.id}" class="btn btn-primary"><i class="icon-edit"></i></a>
			</c:if>
			&nbsp;
	      </td>
          <td>
		    <c:if test="${item.confForm == 0}">
			  <a href="bpm-conf-form-list.do?bpmConfNodeId=${item.id}" class="btn"><i class="icon-edit"></i></a>
			</c:if>
		    <c:if test="${item.confForm == 1}">
			  <a href="bpm-conf-form-list.do?bpmConfNodeId=${item.id}" class="btn btn-primary"><i class="icon-edit"></i></a>
			</c:if>
			&nbsp;
	      </td>
          <td>
		    <c:if test="${item.confOperation == 0}">
			  <a href="bpm-conf-operation-list.do?bpmConfNodeId=${item.id}" class="btn"><i class="icon-edit"></i></a>
			</c:if>
		    <c:if test="${item.confOperation == 1}">
			  <a href="bpm-conf-operation-list.do?bpmConfNodeId=${item.id}" class="btn btn-primary"><i class="icon-edit"></i></a>
			</c:if>
			&nbsp;
	      </td>
          <td>
		    <c:if test="${item.confNotice == 0}">
			  <a href="bpm-conf-notice-list.do?bpmConfNodeId=${item.id}" class="btn"><i class="icon-edit"></i></a>
			</c:if>
		    <c:if test="${item.confNotice == 1}">
			  <a href="bpm-conf-notice-list.do?bpmConfNodeId=${item.id}" class="btn btn-primary"><i class="icon-edit"></i></a>
			</c:if>
			&nbsp;
	      </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<es:page page="${page}"/>
