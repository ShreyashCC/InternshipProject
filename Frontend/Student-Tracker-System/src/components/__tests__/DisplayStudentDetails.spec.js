import { mount, flushPromises } from '@vue/test-utils'
import ShowStudent from '../DisplayStudentDetails.vue'

import { createI18n } from 'vue-i18n'
import axios from 'axios'

jest.mock('axios')

const mockStudent = {
    regNo: '1234',
    rollNo: '56',
    firstName: 'John',
    lastName: 'Doe',
    standard: '11',
    admissionDate: '2021-06-10',
    address: '123 Main St',
    mobileNo: '9876543210',
    emailId: 'john@example.com',
    status: 'ACTIVE'
}

const i18n = createI18n({
    legacy: false,
    locale: 'en',
    messages: {
        en: {
            ShowAllStudents: {
                StudentInformation: 'Student Information',
                RegistrationNo: 'Registration No',
                RollNo: 'Roll No',
                FirstName: 'First Name',
                LastName: 'Last Name',
                Standard: 'Standard',
                AdmissionDate: 'Admission Date',
                Address: 'Address',
                Mobile: 'Mobile',
                Email: 'Email',
                Status: 'Status',
                graduate_student_title: 'Graduate Student?',
                confirm: 'Confirm',
                cancel: 'Cancel',
                generate_tc_title: 'Generate Student TC?',
                promote_student_title: 'Promote Student?',
                success_tc_generated: 'Transfer certificate generated for ID {regNo}',
                failed_to_graduate: 'Failed to graduate!',
            },
            Promote: {
                btnText: 'Promote',
                btnText2: 'Generate TC',
                btnText4: 'Graduate',
                success: 'Student ID {regNo} promoted successfully!',
                failed: 'Failed to promote!'
            }
        }
    }
})

describe('ShowStudent.vue', () => {
    beforeEach(() => {
        jest.clearAllMocks()
    })

    it('fetches and displays student details', async () => {
        axios.get.mockResolvedValueOnce({ data: mockStudent })

        const wrapper = mount(ShowStudent, {
            props: { regNo: '1234' },
            global: { plugins: [i18n] }
        })

        await flushPromises()

        expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/student/1234')
        expect(wrapper.text()).toContain('John')
        expect(wrapper.text()).toContain('Doe')
        expect(wrapper.find('.student-table').exists()).toBe(true)
    })

    it('shows error message if fetch fails', async () => {
        axios.get.mockRejectedValueOnce({ response: { data: { message: 'Not found' } } })

        const wrapper = mount(ShowStudent, {
            props: { regNo: '9999' },
            global: { plugins: [i18n] }
        })

        await flushPromises()
        expect(wrapper.text()).toContain('Not found')
    })

    it('shows promote modal when promote button clicked', async () => {
        axios.get.mockResolvedValueOnce({ data: mockStudent })
        const wrapper = mount(ShowStudent, {
            props: { regNo: '1234' },
            global: { plugins: [i18n] }
        })

        await flushPromises()

        const button = wrapper.find('button.promote-button')
        await button.trigger('click')
        expect(wrapper.text()).toContain('Generate Student TC?')
    })

    it('promotes student and shows success message', async () => {
        axios.get
            .mockResolvedValueOnce({ data: mockStudent }) // fetchStudentDetails
            .mockResolvedValueOnce({})                    // promote call
            .mockResolvedValueOnce({ data: mockStudent }) // fetch again

        const wrapper = mount(ShowStudent, {
            props: { regNo: '1234' },
            global: { plugins: [i18n] }
        })

        await flushPromises()
        await wrapper.vm.showPromoteModel('1234')
        await wrapper.vm.confirmPromote()
        await flushPromises()

        expect(wrapper.text()).toContain('Student ID 1234 promoted successfully!')
    })

    it('handles promote failure', async () => {
        axios.get
            .mockResolvedValueOnce({ data: mockStudent })
            .mockRejectedValueOnce(new Error('Server error'))

        const wrapper = mount(ShowStudent, {
            props: { regNo: '1234' },
            global: { plugins: [i18n] }
        })

        await flushPromises()
        await wrapper.vm.showPromoteModel('1234')
        await wrapper.vm.confirmPromote()
        await flushPromises()

        expect(wrapper.text()).toContain('Failed to promote!')
    })
})
