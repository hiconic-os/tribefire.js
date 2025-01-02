import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    globals: true,
    environment: "node",  // Set to 'jsdom' for UI testing, or 'node' for algorithm-only tests
    // setupFiles: "./test/setup.ts",  // Optional setup file
  },
})