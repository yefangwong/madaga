<script setup>
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
          <header>
            <h1>自然語言轉 SQL</h1>
          </header>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 offset-md-4">
          <main>
              <!-- 自然語言查詢表單開始 -->
              <form role="form">
                <div class="input-group mb-3">
                  <input type="text" class="form-control-lg" size="50" placeholder="請輸入要轉換的查詢語句">
                  <button 
                    class="btn btn-outline-secondary dropdown-toggle" 
                    type="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false">
                    選擇執行算法
                  </button>
                  <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="#">Open AI</a></li>
                  </ul>
                </div>
                <div class="input-group mb-3">
                    <h3 class="typing">SELECT NAME FROM Customer</h3>
                </div>
              </form>
              <!-- 自然語言查詢表單結束 -->
          </main>
        </div>
    </div>
  </div>
</template>

<style>
.typing {
    width: 15em;
    white-space: nowrap;
    border-right: 2px solid transparent;
    animation: typing 3.5s steps(15, end), blink-caret .75s step-end infinite;
    overflow: hidden;
}
/* 打字效果 */
@keyframes typing {
  from { width: 0; }
  to { width: 15em; }
}
/* 光標閃爍 */
@keyframes blink-caret {
  from, to { box-shadow: 1px 0 0 0 transparent; }
  50% { box-shadow: 1px 0 0 0; }
}
</style>
