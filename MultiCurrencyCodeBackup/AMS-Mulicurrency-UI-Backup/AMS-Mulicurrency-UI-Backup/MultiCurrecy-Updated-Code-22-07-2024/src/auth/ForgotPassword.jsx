import { useState, Fragment, React } from "react";
import { Dialog, Transition } from "@headlessui/react";
// import { Link } from "react-router-dom";
// import OtpForms from "../App"
import amsApi from "../api/amsApi";
import CustomAlert from "../layout/CustomAlert";
// import { Navigate } from "react-router-dom";
function ForgotPassword() {
  // message box
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  // const navigate = useNavigate();
  //   const showError = (resMessage) => {
  //     setAlertTitle("Error");
  //     setAlertMessage(resMessage);
  //     setAlertType("red");
  //     setShowAlert(true);
  //   };
  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const [strUserId, setUserId] = useState("");

  const clearHandler = () => {
    setUserId("");
  };

  //   var data = '{"emailId":"' + email + '","mobileNumber":"' + phNumber + '"}';
  //   var key = CryptoJS.enc.Latin1.parse("1234567812345678");
  //   var iv = CryptoJS.enc.Latin1.parse("1234567812345678");
  //   var encrypted = CryptoJS.AES.encrypt(data, key, {
  //     iv: iv,
  //     mode: CryptoJS.mode.CBC,
  //     padding: CryptoJS.pad.ZeroPadding,
  //   });
  //   var decrypted = CryptoJS.AES.decrypt(encrypted, key, {
  //     iv: iv,
  //     padding: CryptoJS.pad.ZeroPadding,
  //   });

  const addDetails = async (event) => {
    event.preventDefault();
    if (strUserId === "") {
      alert("Please provide value in input field!");
    } else {
      try {
        const response = await amsApi.post(
          "usermaster/forgotPassword",
          {
            strUserId: strUserId,
          },
          {
            headers: {
              "content-type": "application/json",
            },
          }
        );
        if (response.status === 200) {
          // navigate("/login", { replace: true });
          openModal();
          handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  let [isOpen, setIsOpen] = useState(false);
  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    setIsOpen(true);
  }

  return (
    <>
      <div>
        <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen ">
          <div className="w-full p-6 bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md dark:bg-gray-800 dark:border-gray-700 sm:p-8">
            <h2 className="mb-1 text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Forgot Password
            </h2>
            <form className="mt-4 space-y-4 lg:mt-5 md:space-y-5" action="#">
              <div>
                <label
                  htmlFor="email"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  UserName
                  <span className="text-red-600 px-2">*</span>
                </label>
                <input
                  type="text"
                  name="strUserId"
                  id="strUserId"
                  value={strUserId}
                  onChange={(e) => setUserId(e.target.value)}
                  className=" border sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-80 p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="Enter Your Current UserName"
                  required=""
                />
              </div>
              <div className="grid grid-cols-4 gap-1">
                <button
                  type="submit"
                  onClick={addDetails}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  type="submit"
                  onClick={clearHandler}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-2 mx-2 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Back
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      {/* Otp Forms  */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={closeModal}>
          <Transition.Child
            as={Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>
          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-3xl transform overflow-hidden rounded-2xl p-6 text-left align-middle transition-all">
                  {/* <OtpForms
                    close={closeModal}
                    emailId={email}
                    phoneNumber={phNumber}
                  /> */}
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>
      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </>
  );
}

export default ForgotPassword;
