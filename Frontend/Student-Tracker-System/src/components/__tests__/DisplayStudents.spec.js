// src/components/__tests__/ShowAllStudents.spec.js
import { mount, flushPromises } from '@vue/test-utils'
import ShowAllStudents from '../DisplayStudents.vue'
import axios from 'axios'

// Mock axios
jest.mock('axios')

// Mock i18n
jest.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: (key, vars) => {
            if (typeof vars === 'object') {
                return `${key} ${Object.values(vars).join(', ')}`
            }
            return key
        }
    })
}))

// Mock child component
jest.mock('../PDFEditor.vue', () => ({
    name: 'PDFEditor',
    template: '<div class="mock-pdf-editor">PDF Editor</div>'
}))

describe('ShowAllStudents.vue', () => {
    const mockStudents = [
        {
            regNo: 'R001',
            rollNo: '101',
            firstName: 'John',
            lastName: 'Doe',
            standard: '11',
            admissionDate: '2020-06-01',
            address: '123 Street',
            mobileNo: '1234567890',
            emailId: 'john@example.com',
            status: 'ACTIVE'
        }
    ]

    beforeEach(() => {
        axios.get.mockReset()
    })

    it('fetches and renders student data', async () => {
        axios.get.mockResolvedValueOnce({ data: mockStudents })

        const wrapper = mount(ShowAllStudents)
        await flushPromises()

        expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/student')
        expect(wrapper.find('td').text()).toContain('R001')
        expect(wrapper.find('.students-table').exists()).toBe(true)
    })

    it('shows error on fetch failure', async () => {
        axios.get.mockRejectedValueOnce({ response: { data: { message: 'Server error' } } })

        const wrapper = mount(ShowAllStudents)
        await flushPromises()

        expect(wrapper.text()).toContain('Server error')
    })

    it('displays modal and triggers graduation', async () => {
        axios.get.mockResolvedValueOnce({ data: mockStudents }) // fetchStudents
        axios.get.mockResolvedValueOnce({}) // graduation API

        const wrapper = mount(ShowAllStudents)
        await flushPromises()

        // Click graduation button
        await wrapper.vm.showGraduationModel('R001')
        expect(wrapper.vm.showGraduationWarning).toBe(true)

        await wrapper.vm.confirmGraduation()
        await flushPromises()

        expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/student/status/R001')
        expect(wrapper.vm.showPDFEditor).toBe(true)
    })

    it('displays modal and promotes student', async () => {
        axios.get.mockResolvedValueOnce({ data: mockStudents }) // fetchStudents
        axios.get.mockResolvedValueOnce({}) // promote API

        const wrapper = mount(ShowAllStudents)
        await flushPromises()

        await wrapper.vm.showPromoteModel('R001')
        expect(wrapper.vm.showPromoteWarning).toBe(true)

        await wrapper.vm.confirmPromote()
        await flushPromises()

        expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/student/promoted/R001')
        expect(wrapper.vm.showSuccess).toBe(true)
    })

    it('renders PDF editor when showPDFEditor is true', async () => {
        axios.get.mockResolvedValueOnce({ data: mockStudents })

        const wrapper = mount(ShowAllStudents)
        await flushPromises()

        wrapper.vm.showPDFEditor = true
        await wrapper.vm.$nextTick()

        expect(wrapper.find('.mock-pdf-editor').exists()).toBe(true)
    })
})
