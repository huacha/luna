<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<table class="sort-table table table-bordered table-hover">
      <thead>
        <tr>
            <th style="width: 80px;" sort="id">编号</th>
            <th style="width: 300px;" sort="name">键</th>
            <th style="width: 400px;">值</th>
            <th>是否显示</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${empty page.content}">
          <tr>
              <td colspan="6">无</td>
          </tr>
      </c:if>
      <c:forEach items="${page.content}" var="extKeyValue">
        <tr>
            <td>${extKeyValue.id}</td>
            <td>${extKeyValue.name}</td>
            <td>${extKeyValue.value}</td>
            <td>${extKeyValue.show ? '是' : '否'}</td>
        </tr>
      </c:forEach>
      </tbody>
  </table>
