import { mount } from '@vue/test-utils'
import WelcomeCard from '../HomeComponent.vue'

// Mock vue-i18n
jest.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: (key) => key // return key as translation
    })
}))

describe('WelcomeCard.vue', () => {
    it('renders the translated title and message', () => {
        const wrapper = mount(WelcomeCard)

        expect(wrapper.text()).toContain('homeContent.name')
        expect(wrapper.text()).toContain('homeContent.showMessage')
    })
})
