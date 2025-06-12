module.exports = {
    testEnvironment: 'jsdom',
    moduleFileExtensions: ['js', 'json', 'vue'],
    transform: {
        '^.+\\.vue$': 'vue-jest',
        '^.+\\.js$': 'babel-jest',
    },
    transformIgnorePatterns: ['/node_modules/(?!axios)'], // transform axios if needed
    moduleNameMapper: {
        '^@/(.*)$': '<rootDir>/src/$1', // if you use @ alias
    },
    testMatch: ['**/__tests__/**/*.spec.[jt]s'],
};
