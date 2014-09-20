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
    <div>
		<div class="control-group span2">
			流程名称: ${bpmProcess.name} 
		</div>

		<div class="control-group span2">
			描述：${bpmProcess.descn} 
		</div>
		
		<div class="control-group span2">
			节点：${bpmConfNode.name} 
		</div>
		
		<div class="control-group span2">
			节点类型：${bpmConfNode.type} 
		</div>
		
	</div>
	
		<br />
		
		<div class="panel">
			<h4 class="hr tool">
				参与者&nbsp;&nbsp;&nbsp;&nbsp;
				
				<div class="btn-group">
	                <a class="btn btn-create-child no-disabled">
	                    <i class="icon-file-alt"></i>
	                    新增
	                </a>
	                <shiro:hasPermission name="bpm:conf:user:update">
	                <a id="update" class="btn btn-update">
	                    <i class="icon-edit"></i>
	                    修改
	                </a>
	                </shiro:hasPermission>
	                <shiro:hasPermission name="bpm:conf:user:delete">
	                <a class="btn btn-delete">
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
							<th style="width: 250px" sort="name">名称</th>
							<th style="width: 80px" sort="type">类型</th>
							<th style="width: 80px" sort="priority">状态</th>
							<th >&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.content}" var="m">
							<tr>
								<td class="check"><input type="checkbox" name="ids"
									value="${m.id}"></td>
								<td><a class="btn btn-link btn-edit"
									href="${ctx}/bpm/process/process/${m.id}">${m.id}</a></td>
								<td>${m.value}</td>
								<td><c:if test="${m.type==0}">
									   	负责人
									   </c:if> <c:if test="${m.type==1}">
									    候选人
									   </c:if> <c:if test="${m.type==2}">
									    候选组
								     </c:if> <c:if test="${m.type==3}">
									  抄送人
									</c:if></td>
								<td><c:if test="${m.status==0}">
									  默认
									</c:if> <c:if test="${m.status==1}">
									  添加
									</c:if> <c:if test="${m.status==2}">
									  删除
									</c:if></td>
									<td></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
			</div>
		</div>
		<br />
		<br />

</div>
<es:contentFooter />

<script type="text/javascript">
    $(function () {
        
        $.parentchild.initParentForm({
            createUrl : "${ctx}/bpm/conf/user/node-${bpmProcess.id}/create",
            updateUrl : "${ctx}/bpm/conf/user/node-${bpmProcess.id}/{id}/update",
            deleteUrl : "${ctx}/bpm/conf/user/node-${bpmProcess.id}/{id}/delete",
            batchDeleteUrl : "${ctx}/bpm/conf/user/node-${bpmProcess.id}//batch/delete"

        });
        
    });
</script>