// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: [
    '@sidebase/nuxt-auth',
    '@nuxtjs/tailwindcss',
    'nuxt-icon'
  ],
  typescript: {
    shim: false
  },
  auth: {
    provider: {
      type: 'local',
      endpoints: {
        getSession: {
          path: '/user'
        }
      },
      pages: {
        login: '/'
      },
      token: {
        signInResponseTokenPointer: '/token'
      },
      sessionDataType: {
        id: 'string',
        name: 'string'
      }
    },
    session: {
      enableRefreshOnWindowFocus: true,
      enableRefreshPeriodically: 5000
    },
    globalAppMiddleware: {
      isEnabled: true
    }
  }
})
