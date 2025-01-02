import { createRequire } from 'module';
const require = createRequire(import.meta.url);
importScripts(require.resolve('monaco-editor/min/vs/base/worker/workerMain'));