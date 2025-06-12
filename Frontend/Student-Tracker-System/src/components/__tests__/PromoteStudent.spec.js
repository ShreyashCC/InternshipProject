import { mount, flushPromises } from '@vue/test-utils'
import PromoteStudent from '../PromoteStudent.vue'
import axios from 'axios'
import { nextTick } from 'vue'

// Mock axios
jest.mock('axios')
// Mock vue-i18n
jest.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: (msg, vars) => {
            if (msg === 'Promote.success') return `Student ID ${vars.regNo} promoted successfully!`
            return msg
        },
    }),
}))

describe('PromoteStudent.vue', () => {
    let wrapper

    beforeEach(() => {
        wrapper = mount(PromoteStudent)
    })

    afterEach(() => {
        jest.clearAllMocks()
    })

    it('renders input field and button', () => {
        expect(wrapper.find('input#studentId').exists()).toBe(true)
        expect(wrapper.find('button[type="submit"]').exists()).toBe(true)
    })

    it('opens confirmation modal when form is submitted', async () => {
        await wrapper.find('input#studentId').setValue('123')
        await wrapper.find('form').trigger('submit.prevent')
        expect(wrapper.vm.showPromoteWarning).toBe(true)
        expect(wrapper.find('.modal-backdrop').exists()).toBe(true)
    })

    it('promotes student successfully and shows success message', async () => {
        axios.get.mockResolvedValueOnce({ data: 'Promoted' })
        wrapper.vm.studentId = '101'
        wrapper.vm.currentStudentRegNo = { value: '101' }
        wrapper.vm.showPromoteWarning = true

        await wrapper.vm.promoteStudent()
        await flushPromises()

        expect(wrapper.vm.showSuccess).toBe(true)
        expect(wrapper.find('.top-notification-success').text()).toContain('101')
    })

    it('handles promotion failure and shows error message', async () => {
        axios.get.mockRejectedValueOnce(new Error('Network Error'))
        wrapper.vm.studentId = '102'
        wrapper.vm.showPromoteWarning = true

        await wrapper.vm.promoteStudent()
        await flushPromises()

        expect(wrapper.vm.showFailed).toBe(true)
        expect(wrapper.find('.top-notification-failed').exists()).toBe(true)
    })

    it('resets studentId after successful promotion', async () => {
        axios.get.mockResolvedValueOnce({ data: 'Promoted' })
        wrapper.vm.studentId = '105'
        wrapper.vm.currentStudentRegNo = { value: '105' }
        wrapper.vm.showPromoteWarning = true

        await wrapper.vm.promoteStudent()
        expect(wrapper.vm.studentId).toBe('')
    })
})

