import api from "../api/axios";

export default function Payment() {
  const pay = async () => {
    const res = await api.post("/payment/create-order");

    const options = {
      key: "rzp_test_xxxxx",
      amount: res.data.amount,
      currency: "INR",
      order_id: res.data.id,
      handler: function () {
        alert("Payment Successful!");
      },
    };

    const rzp = new window.Razorpay(options);
    rzp.open();
  };

  return (
    <div style={{ padding: 40 }}>
      <h2>Payment</h2>
      <button onClick={pay}>Pay Now</button>
    </div>
  );
}