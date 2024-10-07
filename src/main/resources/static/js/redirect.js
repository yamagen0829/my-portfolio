const deleteButtons = document.querySelectorAll('[th\\:data-bs-target^="#deleteReviewModal"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const reviewId = this.getAttribute('data-bs-target').replace('#deleteReviewModal', '');
            const redirectUrl = this.getAttribute('data-redirect-url');
            const deleteForm = document.getElementById('deleteReviewForm' + reviewId);
            if (deleteForm) {
                deleteForm.querySelector('input[name="redirectUrl"]').value = redirectUrl;
            }
        });
    });