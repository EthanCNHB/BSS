import js from '@eslint/js'
import vue from 'eslint-plugin-vue'

export default [
  {
    env: {
      browser: true,
      es2021: true,
      node: true,
      jest: true,
    },
    parser: 'vue-eslint-parser',
    parserOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
      parser: '@typescript-eslint/parser',
    },
    extends: [
      'eslint:recommended',
      'plugin:vue/vue3-essential',
      'plugin:@typescript-eslint/recommended',
      'plugin:prettier/recommended',
    ],
    rules: {
      'no-var': 'error',
      'no-multiple-empty-lines': ['warn', { max: 1 }],
      'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
      'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
      'no-unexpected-multiline': 'error',
      'no-useless-escape': 'off',

      '@typescript-eslint/no-unused-vars': 'error',
      '@typescript-eslint/prefer-ts-expect-error': 'error',
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-non-null-assertion': 'off',
      '@typescript-eslint/no-namespace': 'off',
      '@typescript-eslint/semi': ['error', 'never'], // 禁用分号

      quotes: ['error', 'single'], // 统一使用单引号

      'vue/multi-word-component-names': 'off',
      'vue/script-setup-uses-vars': 'error',
      'vue/no-mutating-props': 'off',
      'vue/attribute-hyphenation': 'off',

      'prettier/prettier': 'error',
    },
    plugins: ['vue', '@typescript-eslint'],
    ignores: ['node_modules/', 'dist/'],
  },
]
