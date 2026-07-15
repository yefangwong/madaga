/**
 * DataToolbar.js - 數據操作按鈕工具列組件
 * 
 * Description:
 * --------------------------------------------------
 * 封存後台管理系統（如員工管理、部門管理）的「新增、修改、刪除」按鈕樣式與排版。
 * 點擊按鈕時透過 $emit 將事件丟回給父級 Vue 實例處理，實現樣式與業務邏輯的解耦。
 * 
 * History:
 * --------------------------------------------------
 * 2026/07/15 yfwong - 初始建立，成功整合至員工管理與部門管理視圖
 */
Vue.component('data-toolbar', {
    template: `
        <div class="d-grid gap-2 d-md-block" role="toolbar" style="margin-top: 15px;">
            <button type="button" id="add" class="btn btn-primary csp-btn-spacing" @click="$emit('add')">
                <i class="fa fa-plus pe-1" aria-hidden="true"></i> 新增
            </button>
            <button type="button" id="update" class="btn btn-primary csp-btn-spacing" @click="$emit('edit')">
                <i class="fa fa-pencil pe-1" aria-hidden="true"></i> 修改
            </button>
            <button type="button" id="delete" class="btn btn-danger csp-btn-spacing" @click="$emit('delete')">
                <i class="fa fa-trash pe-1" aria-hidden="true"></i> 刪除
            </button>
        </div>
    `
});
