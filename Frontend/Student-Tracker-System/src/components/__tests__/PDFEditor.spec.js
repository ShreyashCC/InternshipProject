import { mount } from '@vue/test-utils'
import PDFEditor from '../PDFEditor.vue'

describe('PDFEditor.vue', () => {
    it('renders iframe with correct src from regNo prop', () => {
        const wrapper = mount(PDFEditor, {
            props: { regNo: '123' }
        })

        const iframe = wrapper.find('iframe')
        expect(iframe.exists()).toBe(true)
        expect(iframe.attributes('src')).toBe('http://localhost:8080/pdf/generate/123')
    })

    it('emits goBack when button is clicked', async () => {
        const wrapper = mount(PDFEditor, {
            props: { regNo: '123' }
        })

        await wrapper.find('button').trigger('click')
        expect(wrapper.emitted()).toHaveProperty('goBack')
        expect(wrapper.emitted('goBack').length).toBe(1)
    })
})
