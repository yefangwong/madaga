import './cornelius-ui/index.css';
import { CorneliusUI } from './cornelius-ui/index.js';

// Expose CorneliusUI globally for interactive playground demo
window.CorneliusUI = CorneliusUI;

// One-click Code Copy Helper for Playground
window.copyCode = function(btn) {
  const container = btn.closest('.code-editor');
  if (!container) return;
  const codeEl = container.querySelector('.code-content');
  if (!codeEl) return;
  
  const text = codeEl.innerText;
  navigator.clipboard.writeText(text).then(() => {
    const origHtml = btn.innerHTML;
    btn.innerHTML = '<i class="fa-solid fa-check" style="color: #10b981;"></i> 已複製！';
    btn.style.borderColor = '#10b981';
    btn.style.color = '#10b981';
    setTimeout(() => {
      btn.innerHTML = origHtml;
      btn.style.borderColor = '';
      btn.style.color = '';
    }, 2000);
  }).catch(err => {
    console.error('Copy failed', err);
  });
};

console.log('🌟 CorneliusUI Design System Playground Initialized');

