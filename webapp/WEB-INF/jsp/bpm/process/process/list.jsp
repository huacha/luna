<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <es:showMessage/>

    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
            <div class="btn-group">
                <shiro:hasPermission name="bpm:process:process:create">
                <a class="btn btn-create">
                    <i class="icon-file-alt"></i>
                    新增
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bpm:process:process:update">
                <a id="update" class="btn btn-update">
                    <i class="icon-edit"></i>
                    修改
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bpm:process:process:delete">
                <a class="btn btn-delete">
                    <i class="icon-trash"></i>
                    删除
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bpm:process:process:config">
                <a  id="config" class="btn btn-link config-process">
                    <i class="icon-cog"></i>
                    配置
                </a>
                </shiro:hasPermission>
            </div>
        </div>
        <div class="span8">
            <%@include file="searchForm.jsp"%>
        </div>
    </div>
    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>

<script type="text/javascript">
    $(function() {

        $(".config-process").click(function() {

            var checkbox = $.table.getFirstSelectedCheckbox($(".table"));
            if(checkbox.size() == 0) {
                return;
            }
            var id = checkbox.val();
            
            window.location.href=
                    "${ctx}/bpm/conf/node/process-"+ id + "" +
                            "?BackURL=" + $.table.tableURL($(".table"));
            
        });

    });
</script>