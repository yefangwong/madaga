/**
 * A.js - 我的第一個 Vue 組件
 * 
 * History:
 * --------------------------------------------------
 * 2026/07/15 yfwong - 初始建立，驗證 Progressive Vue CDN 下的自訂組件可行性
 */
Vue.component('component-a', {
    template: `
        <div class="card p-3 my-2 shadow-sm">
            <h5>{{ title }}</h5>
            <p>這是組件 A 的內容：<strong>{{ localMsg }}</strong></p>
            <button @click="handleClick" class="btn btn-primary btn-sm">點擊我</button>
        </div>
    `,
    props: ['title'],
    data() {
        return {
            localMsg: '來自組件內部的狀態'
        };
    },
    methods: {
        handleClick() {
            alert('組件 A 被點擊了！');
        }
    }
});