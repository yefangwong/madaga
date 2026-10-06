<script setup>
import { computed } from 'vue'

const props = defineProps({
  label: String,
  type: {
    type: String,
    default: 'button'
  },
  color: {
    type: String,
    default: 'primary' // primary, secondary, etc.
  },
  size: {
    type: String,
    default: 'md' // sm, md, lg
  },
  block: Boolean,
  loading: Boolean,
  disabled: Boolean
})

const emit = defineEmits(['click'])

const classes = computed(() => {
  return [
    'c-btn',
    `c-btn-${props.color}`,
    `c-btn-${props.size}`,
    { 'is-loading': props.loading, 'c-btn-block': props.block }
  ]
})

const handleClick = (e) => {
  if (!props.disabled && !props.loading) {
    emit('click', e)
  }
}
</script>

<template>
  <button :type="type" :class="classes" :disabled="disabled || loading" @click="handleClick">
    <span v-if="loading" class="spinner"></span>
    <slot v-else>{{ label }}</slot>
  </button>
</template>

<style>
/* 針對 Custom Element 容器本身的樣式 (:host 代表 <cornelius-button> 本身) */
:host {
  display: inline-block; /* 讓自定義標籤具備物理體積 */
  /* 當外部下達 flex: 1 或寬度時，容器能正常擴展 */
}

/* 滿版屬性：如果在標籤上加上 block，則轉為區塊元素 */
:host([block]) {
  display: block;
  width: 100%;
}

.c-btn {
  /* 讓內部的按鈕永遠填滿外部 <cornelius-button> 容器 */
  width: 100%;
  height: 100%;

  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  letter-spacing: 0.5px;
  border: none;
  cursor: pointer;
  transition: var(--cui-transition, all 0.22s cubic-bezier(0.4, 0, 0.2, 1));
  font-family: var(--cui-font-base, inherit);
  box-sizing: border-box;
  position: relative;
  overflow: hidden;
}

/* 尺寸定義 (Size Tokens) */
.c-btn-sm {
  padding: 6px 16px;
  font-size: 12px;
  border-radius: var(--cui-radius-sm, 8px);
}
.c-btn-md {
  padding: 10px 24px;
  font-size: 14px;
  border-radius: var(--cui-radius-md, 14px);
}
.c-btn-lg {
  padding: 14px 32px;
  font-size: 16px;
  border-radius: var(--cui-radius-lg, 20px);
}

/* 滿版寬度 (透過 class 控制) */
.c-btn-block {
  display: flex;
  width: 100%;
}

.c-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
  box-shadow: none !important;
}

/* Apple HIG 風格的實體按壓反饋：整顆按鈕微縮並變暗 (移除 Quasar 的水波紋) */
.c-btn:active:not(:disabled) {
  transform: scale(0.96);
  filter: brightness(0.85);
  /* 關鍵！按下瞬間取消動畫延遲，讓輕按也能瞬間獲得強烈反饋 */
  transition: none;
}

/* 模擬 Quasar / Material Design 的打亮 Overlay */
.c-btn::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: currentColor;
  opacity: 0;
  transition: opacity 0.25s ease;
  pointer-events: none;
  border-radius: inherit;
}

.c-btn:hover:not(:disabled)::before {
  opacity: 0.12; /* 滑過時加上字體顏色的半透明遮罩，讓主色變淡/亮 */
}

.c-btn-primary {
  /* 使用 Madaga CSP (鴻方) 的專屬設計 Token */
  background: var(--cui-primary, #E26D38);
  color: white;
  /* 換成 Material Design (Quasar) 標準的超柔和陰影 (Elevation 2) */
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.2), 0 2px 2px rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.12);
}

.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 0.8s linear infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
