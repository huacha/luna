<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<div id="container" data-table="childTable">
    <div class="row-fluid tool ui-toolbar">
        <form id="searchForm" class="form-inline search-form" data-change-search="true">

		    <esform:label path="search.id_in">编号</esform:label>
		    <esform:input path="search.id_in" cssClass="input-small" placeholder="多个使用空格分隔"/>
		    &nbsp;&nbsp;
		
		    <esform:label path="search.username_like">用户名</esform:label>
		    <esform:input path="search.username_like" cssClass="input-small" placeholder="模糊匹配"/>
		    &nbsp;&nbsp;
		
		    <esform:label path="search.status_eq">状态</esform:label>
		    <esform:select path="search.status_eq" cssClass="input-small">
		        <esform:option label="所有" value=""/>
		        <esform:options items="${statusList}" itemLabel="info"/>
		    </esform:select>
		
		    &nbsp;&nbsp;
		    <input type="submit" class="btn" value="查询"/>
		    <a class="btn btn-link accordion-toggle" data-toggle="collapse" href="#searchMore">高级查询</a>
		    <a class="btn btn-link btn-clear-search">清空</a>
		
		
		    <%--more--%>
		    <div id="searchMore" class="accordion-body collapse">
		        <div class="accordion-inner">
		            <esform:label path="search.createDate_gte">创建时间从</esform:label>
		
		            <div class="input-append date">
		                <esform:input path="search.createDate_gte" cssClass="input-medium"
		                              data-format="yyyy-MM-dd hh:mm:ss"
		                              placeholder="大于等于"/>
		                <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
		            </div>
		            <esform:label path="search.createDate_lte">到</esform:label>
		            <div class="input-append date">
		                <esform:input path="search.createDate_lte" cssClass="input-medium"
		                              data-format="yyyy-MM-dd hh:mm:ss"
		                              data-position="bottom-left"
		                              placeholder="小于等于"/>
		                <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
		            </div>
		        </div>
		    </div>
		</form>
    </div>
    <div>
      <table id="selectTable" class="sort-table table table-bordered table-hover"
              data-async="true"
              data-async-container="container">
          <thead>
            <tr>

                <th sort="id">编号</th>
                <th sort="username">名称</th>
                <th style="width: 80px">选择</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${page.content}" var="m" varStatus="status">
            <tr id="${m.id}">

                <td>${m.id}</td>
                <td class="name">${m.username}</td>
                <td class="check">
                    <c:choose>
                        <c:when test="${selectType eq 'multiple'}">
                            <input type="checkbox" name="ids" value="${m.username}">
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="ids" value="${m.username}">
                        </c:otherwise>
                    </c:choose>

                </td>
            </tr>
          </c:forEach>
          </tbody>
      </table>
      <es:page page="${page}"/>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        var table = $("#selectTable");
        $.table.initTable(table);

        var hasDomName = ${not empty domName};
        //参照窗口的dom
        var $openerId = $("#${domId}");
        
        
        var ids = $.array.trim($openerId.val().split(","));
        if(hasDomName) {
            var $openerName = $("#${domName}");
            var names = $.array.trim($openerName.val().split(","));
        }
        //当前窗口的checkbox / radion
        var $checkboxOrRadio = table.find("td.check").find(":checkbox,:radio");
        //根据参照窗口的值 选中当前窗口的数据
        $checkboxOrRadio.each(function() {
            var $current = $(this);
            var $currentVal = $current.val();
            $.each(ids, function(index, id) {
                if(id == $currentVal) {
                    $current.prop("checked", true);
                }
            });
        });
        $checkboxOrRadio.closest("tr").andSelf().click(function() {
            var $current = $(this);
            if($current.is("tr")) {
                $current = $current.find(":checkbox,:radio");
            }
            //单选按钮仅有一个值
            if($current.is(":radio")) {
                $.array.clear(ids);
                if(hasDomName) {
                    $.array.clear(names);
                }
            }
            var id = $current.val();

            if(hasDomName) {
                var name = $current.closest("tr").find("td.name").text();
            }
            if ($current.is(":checked")) {
                if (!$.array.contains(ids, id)) {
                    ids.push(id);
                    if(hasDomName) {
                        names.push(name);
                    }
                }
            } else {
                var index = $.array.indexOf(ids, id);
                ids.splice(index, 1);
                if(hasDomName) {
                    names.splice(index, 1);
                }
            }

            $openerId.val(ids.join(","));

            if(hasDomName) {
                $openerName.val(names.join(","));
            }

        });


    });
</script>