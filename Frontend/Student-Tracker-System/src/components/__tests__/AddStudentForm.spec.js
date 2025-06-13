import { mount, flushPromises } from '@vue/test-utils'
import AddStudentForm from '../AddStudent.vue'
import axios from 'axios'

// Mock axios
jest.mock('axios')

// Mock i18n
jest.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: (key) => key
    })
}))

describe('AddStudentForm.vue', () => {
    let wrapper

    beforeEach(() => {
        jest.clearAllMocks()
        wrapper = mount(AddStudentForm)
    })

    it('renders correctly', () => {
        expect(wrapper.text()).toContain('SideBar.AddStudent')
    })

    it('shows error if firstName or lastName contains number', async () => {
        const inputs = wrapper.findAll('input')
        await inputs[0].setValue('123') // firstName with number
        await wrapper.find('form').trigger('submit.prevent')
        await flushPromises()

        expect(wrapper.html()).toContain('AddNewStudent.error_firstname_lastname_number')
    })

    it('shows error if standard is invalid', async () => {
        const inputs = wrapper.findAll('input')

        await inputs[0].setValue('John')   // firstName
        await inputs[1].setValue('Doe')    // lastName
        await inputs[2].setValue('15')     // standard (invalid)
        await inputs[3].setValue('Address')
        await inputs[4].setValue('9999999999')
        await inputs[5].setValue('test@example.com')

        await wrapper.find('form').trigger('submit.prevent')
        await flushPromises()

        expect(wrapper.html()).toContain('AddNewStudent.error_standard_range')
    })

    it('submits successfully with valid data', async () => {
        axios.post.mockResolvedValueOnce({ data: { id: 1 } })

        const inputs = wrapper.findAll('input')
        await inputs[0].setValue('John')
        await inputs[1].setValue('Doe')
        await inputs[2].setValue('5')
        await inputs[3].setValue('Address')
        await inputs[4].setValue('9999999999')
        await inputs[5].setValue('test@example.com')

        await wrapper.find('form').trigger('submit.prevent')
        await flushPromises()

        expect(axios.post).toHaveBeenCalled()
        expect(wrapper.html()).toContain('AddNewStudent.success_student_added')
    })

    it('shows error when axios throws', async () => {
        axios.post.mockRejectedValueOnce(new Error('Network Error'))

        const inputs = wrapper.findAll('input')
        await inputs[0].setValue('John')
        await inputs[1].setValue('Doe')
        await inputs[2].setValue('5')
        await inputs[3].setValue('Address')
        await inputs[4].setValue('9999999999')
        await inputs[5].setValue('test@example.com')

        await wrapper.find('form').trigger('submit.prevent')
        await flushPromises()

        expect(wrapper.html()).toContain('AddNewStudent.error_student_add_failed')
    })
})
