　document.addEventListener('DOMContentLoaded', function() {
	const stripe = Stripe('pk_test_51PUq0jFSM1uSKiJ0JBLJ3sysAhyanZOVZ59IzigAE37h2P2ru4x8MupM8pkBfY7zbSu4RKTEeQAAHM73QgdytHGu00Q0SApZAq');
    const elements = stripe.elements();
    const cardElement = elements.create('card', { hidePostalCode: true });
    
    const checkoutForm = document.querySelector('#checkout-form');
    const editCardButton = document.querySelector('#edit-card-button');
    const deleteCardButton = document.querySelector('#delete-card');
    const saveCardButton = document.querySelector('#save-card');
    
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;	
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    
    console.log('CSRF Header:', csrfHeader); 
    console.log('CSRF Token:', csrfToken); 
    console.log('User Email:', email);
    
     // ヘッダーの値を格納
    const headers = { 'Content-Type': 'application/json' };
    
    console.log('Headers:', headers);

    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function (event) {
            event.preventDefault();
            const emailInput = document.querySelector('#email').value;

            console.log('Email Input:', emailInput);

            fetch('/user/checkout', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({ email: emailInput })
            }).then(response => response.json())
              .then(data => {

                  if (data.sessionId) {
                      return stripe.redirectToCheckout({ sessionId: data.sessionId });
                  } else {
                      alert('Session ID is not defined.');
                  }
              })
              .then(function (result) {
                  if (result && result.error) {
                      alert(result.error.message);
                  }
              })
              .catch(error => {
                  console.error('Error:', error);
                  alert('セッションの作成に失敗しました。もう一度お試しください。');
              });
        });
    }
    
    if (editCardButton) {
        editCardButton.addEventListener('submit', function(event) {
            event.preventDefault();
            const emailInput = document.querySelector('#email').value;

            console.log('Email Input:', emailInput);
            
            fetch('/user/billingPortal', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({ email: emailInput })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.url) {
                    window.location.href = data.url;  // BillingポータルのURLにリダイレクト
                } else {
                    alert('Billingポータルの生成に失敗しました。');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('エラーが発生しました。');
            });
        });
      }
    
    if (saveCardButton) {
        saveCardButton.addEventListener('click', async (event) => {
            event.preventDefault();

            const { paymentMethod, error } = await stripe.createPaymentMethod({
                type: 'card',
                card: cardElement,
            });

            if (error) {
                console.error(error);
            } else {
				console.log('Payment Method ID:', paymentMethod.id); // この行を追加して確認
				fetch('/api/updatePaymentMethod', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify({
                        paymentMethodId: paymentMethod.id,
                    })
                }).then(response => response.json())
                .then(function(data)  {
                    if (data.success) {
                        alert('カード情報が更新されました');
                        cardElement.unmount();
                        document.getElementById('card-info-form').style.display = 'none';
                    } else {
                        alert('カード情報の更新に失敗しました');
                    }
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
            }
        });
    }

    if (deleteCardButton) {
        deleteCardButton.addEventListener('click', function() {
			const paymentMethodId = document.querySelector('#payment-method-id').value; // 実際のpaymentMethodIdをフォームから取得
            
            fetch('/api/deletePaymentMethod', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({
                    paymentMethodId: paymentMethodId,
                })
            })
            .then(response => response.json())
            .then(function(data) {
                if (data.success) {
                    alert('カード情報が削除されました');
                    cardElement.unmount();
                    document.getElementById('card-info-form').style.display = 'none';
                } else {
                    alert('カード情報の削除に失敗しました');
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
        });
    }
});