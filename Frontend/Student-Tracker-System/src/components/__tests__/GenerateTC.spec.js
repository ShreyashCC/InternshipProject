// src/components/__tests__/GenerateTC.spec.js
import { mount, flushPromises } from '@vue/test-utils'
import GenerateTC from '../GenerateTC.vue'
import axios from 'axios'

// Mock axios
jest.mock('axios')

// Mock i18n
jest.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: (key) => key
    })
}))

// Mock PDFEditor
jest.mock('../PDFEditor.vue', () => ({
    name: 'PDFEditor',
    template: '<div class="mock-pdf-editor">PDF Editor</div>',
    props: ['regNo']
}))

describe('GenerateTC.vue', () => {
    beforeEach(() => {
        axios.get.mockReset()
    })

    it('renders input and button elements', () => {
        const wrapper = mount(GenerateTC)
        expect(wrapper.find('input[type="number"]').exists()).toBe(true)
        expect(wrapper.find('button').exists()).toBe(true)
    })

    it('shows modal on clicking generate button', async () => {
        const wrapper = mount(GenerateTC)
        const input = wrapper.find('input')
        await input.setValue('1')

        await wrapper.find('button').trigger('click')

        expect(wrapper.text()).toContain('ShowAllStudents.generate_tc_title')
        expect(wrapper.vm.showGenerateTCWarning).toBe(true)
    })

    it('cancels modal when cancelFxn is called', async () => {
        const wrapper = mount(GenerateTC)
        wrapper.vm.showGenerateTCWarning = true
        await wrapper.vm.cancelFxn()
        expect(wrapper.vm.showGenerateTCWarning).toBe(false)
    })

    it('successfully generates TC and shows PDFEditor', async () => {
        axios.get.mockResolvedValueOnce({}) // for updateStatusToRESCINDED

        const wrapper = mount(GenerateTC)
        wrapper.vm.currentStudentRegNo = '1'
        wrapper.vm.showGenerateTCWarning = true

        await wrapper.vm.updateStatusToRESCINDED()
        await flushPromises()

        expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/student/status/tc/1')
        expect(wrapper.vm.TC).toBe(true)
        expect(wrapper.find('.mock-pdf-editor').exists()).toBe(true)
    })

    it('handles TC generation failure', async () => {
        axios.get.mockRejectedValueOnce(new Error('Network error'))

        const wrapper = mount(GenerateTC)
        wrapper.vm.currentStudentRegNo = '1'
        wrapper.vm.showGenerateTCWarning = true

        await wrapper.vm.updateStatusToRESCINDED()
        await flushPromises()

        expect(wrapper.vm.TC).toBe(null)
        expect(wrapper.vm.showFailed).toBe(true)

        // Wait for the failure message to disappear after 3s
        jest.advanceTimersByTime(3000)
    })
})
