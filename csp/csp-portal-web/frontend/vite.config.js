import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  root: '.',
  server: {
    port: 5173,
    open: false,
    cors: true,
    proxy: {
      // 代理 Spring Boot 後端動態頁面、靜態資源 (/css, /images, /js) 與 API
      '^/(index|index_zh|index_en|department|emp|dashboard|energy|manage|api|auth|login|logout|sustainability|suite|css|images|js|fonts)': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false,
        ws: true,
        configure: (proxy, _options) => {
          proxy.on('error', (err, req, res) => {
            console.warn(`\n⚠️  [Vite Proxy 提示] 無法連線至後端 Spring Boot (http://localhost:8081${req.url})`);
            console.warn(`    請確認 Spring Boot (Port 8081) 是否已啟動。\n`);
            if (res && !res.headersSent && res.writeHead) {
              res.writeHead(502, { 'Content-Type': 'text/html; charset=utf-8' });
              res.end(`
                <div style="font-family: sans-serif; padding: 40px; text-align: center;">
                  <h2 style="color: #E26D38;">⚠️ 後端 Spring Boot 尚未啟動 (ECONNREFUSED: 8081)</h2>
                  <p style="color: #6B6661;">您正在訪問後端動態路由 <code>${req.url}</code>，請先啟動後端 Spring Boot (Port 8081)。</p>
                  <p><a href="/" style="color: #E26D38; font-weight: 600;">👉 返回 CorneliusUI 元件遊樂場 (無需後端)</a></p>
                </div>
              `);
            }
          });
        },
      },
    },
  },
  build: {
    outDir: resolve(__dirname, '../src/main/resources/static/dist'),
    emptyOutDir: true,
    manifest: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        'cornelius-ui': resolve(__dirname, 'src/cornelius-ui/index.js'),
      },
      output: {
        entryFileNames: 'js/[name].js',
        chunkFileNames: 'js/[name]-[hash].js',
        assetFileNames: (assetInfo) => {
          if (assetInfo.name && assetInfo.name.endsWith('.css')) {
            return 'css/[name].[ext]';
          }
          return 'assets/[name]-[hash].[ext]';
        },
      },
    },
  },
});
