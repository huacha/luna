<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader />
<div data-table="table"  class="panel">

    <es:showMessage/>
    
    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
                
	        <a href="<es:BackURL/>">
	            <i class="icon-reply"></i>
	            返回
	        </a>
        </div>
    </div>
		
    <h4 class="hr tool"> 流程节点</h4>
    <div class="span9">
		<table id="nodeTable" class="table table-bordered table-hover" >
			<tr>
				<td style="width: 100px; text-align:right" >流程名称:</td>
				<td style="width: 250px">${bpmProcess.name} </td>
				<td style="width: 100px; text-align:right" align="right">描述：</td>
				<td style="width: 250px">${bpmProcess.descn} </td>
			</tr>
			<tr>
				<td style="text-align:right">节点:</td>
				<td >${bpmConfNode.name} </td>
				<td style="text-align:right">节点类型:</td>
				<td >${bpmConfNode.type} </td>
			</tr>
		</table>
	</div>
	
		<br />
		
		<div class="panel">
			<h4 class="hr tool">
				参与者&nbsp;&nbsp;&nbsp;&nbsp;
				
				<div class="btn-group">
	                <shiro:hasPermission name="bpm:conf:user:update">
	                <a class="btn btn-create-child no-disabled">
	                    <i class="icon-file-alt"></i>
	                    新增
	                </a>
	                </shiro:hasPermission>
	                <shiro:hasPermission name="bpm:conf:user:update">
	                <a id="update" class="btn btn-update-child">
	                    <i class="icon-edit"></i>
	                    修改
	                </a>
	                </shiro:hasPermission>
	                <shiro:hasPermission name="bpm:conf:user:delete">
	                <a class="btn btn-delete-child">
	                    <i class="icon-trash"></i>
	                    删除
	                </a>
	                </shiro:hasPermission>
	            </div>
			</h4>
			
			<div class="span9">
			
				<table id="childTable" class="sort-table table table-bordered table-hover">
					<thead>
						<tr>
							<th style="width: 80px"><a class="check-all"
								href="javascript:;">全选</a> | <a class="reverse-all"
								href="javascript:;">反选</a></th>
							<th style="width: 80px" sort="id">编号</th>
							<th style="width: 250px" sort="value">名称</th>
							<th style="width: 80px" sort="type">类型</th>
							<th style="width: 80px" sort="status">数据来源</th>
							<th >&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.content}" var="m">
							<tr id="old_${m.id}">
								<td class="check"><input type="checkbox" name="ids"
									value="${m.id}"></td>
								<td>${m.id}</td>
								<td>${m.showValue}</td>
								<td>
								<c:forEach items="${bpmconfusertype}" var="item">
								  <c:if test="${item.name eq m.type}">
								    ${item.value}
								  </c:if>  
							    </c:forEach>
							    </td>
								<td>
								<c:forEach items="${bpmconfdatasource}" var="item">
								  <c:if test="${item.name eq m.status}">
								    ${item.value}
								  </c:if>  
							    </c:forEach>
							    </td>
							    <td></td>
						</c:forEach>
					</tbody>
				</table>
				<es:page page="${page}"/>
			</div>
		</div>
		<br />
		<br />

</div>
<es:contentFooter />

<script type="text/javascript">
    $(function () {
        $.noparentchild.initParentTable({
            form : $("#editForm"),
            tableId : "childTable",
            prefixParamName : "m",
            modalSettings:{
                width:600,
                height:450,
                noTitle : true,
                buttons:{}
            },

            createUrl :      "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/create?BackURL=" +$.table.tableURL($(".table")),
            //createUrl :      "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/create",
            updateUrl :      "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/{id}/update?BackURL=" +$.table.tableURL($(".table")),
            deleteUrl :      "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/{id}/delete?BackURL=${ctx}/bpm/conf/user/node-${bpmConfNode.id}",
            //batchDeleteUrl : "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/batch/delete?BackURL=${ctx}/bpm/conf/user/node-${bpmConfNode.id}"
            batchDeleteUrl : "${ctx}/bpm/conf/user/node-${bpmConfNode.id}/batch/delete"

        });
        
    });
</script>