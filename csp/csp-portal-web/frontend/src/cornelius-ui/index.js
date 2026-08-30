/**
 * CorneliusUI Design System - Core JS Library
 */
import './index.css';

export class CorneliusUI {
  /**
   * Open a dialog modal by element ID
   * @param {string} id 
   */
  static openDialog(id) {
    const el = document.getElementById(id);
    if (el) {
      el.classList.add('active');
      document.body.style.overflow = 'hidden';
    }
  }

  /**
   * Share current URL or custom data via Web Share API or copy to clipboard
   * @param {Object} [data]
   */
  static share(data = {}) {
    const shareData = {
      title: data.title || document.title || 'HongFang Portal',
      text: data.text || 'HongFang Portal Dashboard',
      url: data.url || window.location.href,
    };

    if (navigator.share) {
      navigator.share(shareData).catch((err) => {
        if (err.name !== 'AbortError') console.warn('Share failed:', err);
      });
    } else {
      // Fallback: Copy link to clipboard
      navigator.clipboard.writeText(shareData.url).then(() => {
        alert('連結已成功複製至剪貼簿！');
      }).catch(() => {
        prompt('請複製以下連結：', shareData.url);
      });
    }
  }

  /**
   * Export HTML table to CSV file
   * @param {string|HTMLTableElement} tableElementOrSelector 
   * @param {string} [filename='export.csv'] 
   */
  static exportTableToCSV(tableElementOrSelector, filename = 'export.csv') {
    const table = typeof tableElementOrSelector === 'string'
      ? document.querySelector(tableElementOrSelector)
      : tableElementOrSelector;

    if (!table) {
      console.warn('Table not found for export:', tableElementOrSelector);
      return;
    }

    const rows = Array.from(table.querySelectorAll('tr'));
    const csvContent = rows.map((row) => {
      const cells = Array.from(row.querySelectorAll('th, td'));
      return cells.map((cell) => {
        let text = cell.innerText.replace(/"/g, '""').trim();
        return `"${text}"`;
      }).join(',');
    }).join('\r\n');

    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    link.style.display = 'none';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}

// Attach to window object for non-module scripts
if (typeof window !== 'undefined') {
  window.CorneliusUI = CorneliusUI;
}

export default CorneliusUI;

