import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { Dialog, Transition } from "@headlessui/react";
export default function CreditCardconfigurationGL() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [error, setError] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [strGLAccountType, setstrGLAccountType] = useState("");
  const [alldata2, setalldata2] = useState([]);
  const [gstGlAccountType, setgstGlAccountType] = useState("");
  const [latePaymentGlAccountType, setlatePaymentGlAccountType] = useState("");
  const [transactionChargeAccountType, settransactionChargeAccountType] =
    useState("");
  const [cardGlAccountType, setcardGlAccountType] = useState("");
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
  // GL Account Creation List
  useEffect(() => {
    gLAccountCreationlist();
  }, []);
  const gLAccountCreationlist = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/getGlAccTypeAccNumberList
`,
        {}
      );
      if (response.data.code === "S0000") {
        setalldata2(response.data.gLAccountCreationlist);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    Accounttype();
  }, []);
  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getYCrditAccounType`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error);
    }
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (accounttype === "" || strGLAccountType.length === 0) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `creditcardconfigtype/validateCreditCardConfigType`,
          {
            strAccountType: accounttype,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data === true) {
          handleShowError("Data is Already Exist for This AccountType");
        } else {
          const response1 = await amsApi.post(`creditcardconfigtype/add`, {
            strParticipantID: participantID,
            strAccountType: accounttype,
            intrestGlAccountType: strGLAccountType.split("-")[0],
            intrestGlAccountNo: strGLAccountType.split("-")[1],
            gstGlAccountType: gstGlAccountType.split("-")[0],
            gstGlAccountNo: gstGlAccountType.split("-")[1],
            latePaymentGlAccountType: latePaymentGlAccountType.split("-")[0],
            latePaymentGlAccountNo: latePaymentGlAccountType.split("-")[1],
            transactionChargeAccountType:
              transactionChargeAccountType.split("-")[0],
            transactionChargeAccountNo:
              transactionChargeAccountType.split("-")[1],
            cardGlAccountType: cardGlAccountType.split("-")[0],
            cardGlAccountNo: cardGlAccountType.split("-")[1],
          });
          if (response1.data.code === "S0000") {
            handleShowSuccess(response1.data.message);
            setError("");
            handleClick();
          } else {
            handleShowError(response1.data.message);
          }
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };
  // View GST Type
  const [resdata, setResData] = useState("");
  useEffect(() => {
    viewgst();
  }, []);

  const viewgst = async () => {
    try {
      const response = await amsApi.post(
        `creditcardconfigtype/viewCreditCardConfigType`,
        {
          pageSize: "25",
          pageNumber: "1",
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setResData(response.data.cardConfigType);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  const [isOpen, setIsOpen] = useState(false);
  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };
  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };
  const handleClick = () => {
    setAccountType("");
    setlatePaymentGlAccountType("");
    setgstGlAccountType("");
    setstrGLAccountType("");
    settransactionChargeAccountType("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Credit Card Configuration GL
              </p>
            </div>
          </div>
          <div className=" md:col-span-2 lg:col-span-1">
            <form className="" onSubmit={handleSubmit}>
              <div className="overflow-hidden shadow ">
                <div className=" px-4 sm:p-3 bg-white  ">
                  <div className="grid gap-2  lg:grid-cols-1 px-4">
                    <div className="flex">
                      <label
                        htmlFor=" Account Type"
                        className="block w-auto px-2 py-1 mr-9    text-sm font-normal text-gray-700 bg-white  "
                      >
                        Account Type{""}
                        <span className="text-red-600 px-3">*</span>
                      </label>
                      <select
                        id="accountType"
                        name="accountType"
                        value={accounttype}
                        onChange={(e) => setAccountType(e.target.value)}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {type.map((data) => (
                          <option value={data.strAccountType}>
                            {data.strAccountType || "-"} -
                            {data.strDescription || "-"}
                          </option>
                        ))}
                      </select>
                      {error && accounttype.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please enter Account Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="GL Account Type"
                        className="block w-auto px-3 py-1 mr-14  text-sm font-normal text-gray-700 bg-white  "
                      >
                        Interest GL <span className="text-red-600 px-1">*</span>
                      </label>
                      <select
                        name="strGLAccountType"
                        id="strGLAccountType"
                        value={strGLAccountType}
                        onChange={(e) => setstrGLAccountType(e.target.value)}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option className="capatlize text-xs">
                            {data.strGLAccountType}-{data.strGLAccountNumber}
                          </option>
                        ))}
                      </select>
                      {error && strGLAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Interest GL!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="GL Account Type"
                        className="block w-auto px-3 py-1 mr-20  text-sm font-normal text-gray-700 bg-white"
                      >
                        GST GL <span className="text-red-600 px-1">*</span>
                      </label>
                      <select
                        name="gstGlAccountType"
                        id="gstGlAccountType"
                        value={gstGlAccountType}
                        onChange={(e) => setgstGlAccountType(e.target.value)}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option className="capatlize text-xs">
                            {data.strGLAccountType}-{data.strGLAccountNumber}
                          </option>
                        ))}
                      </select>
                      {error && gstGlAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select GST GL!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="GL Account Type"
                        className="block w-auto px-1 py-1 mr-9 text-sm font-normal text-gray-700 bg-white  "
                      >
                        Late Payment GL{" "}
                        <span className="text-red-600 px-1 ">*</span>
                      </label>
                      <select
                        name="latePaymentGlAccountType"
                        id="latePaymentGlAccountType"
                        value={latePaymentGlAccountType}
                        onChange={(e) =>
                          setlatePaymentGlAccountType(e.target.value)
                        }
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option className="capatlize text-xs">
                            {data.strGLAccountType}-{data.strGLAccountNumber}
                          </option>
                        ))}
                      </select>
                      {error && latePaymentGlAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Late Payment GL!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="GL Account Type"
                        className="block w-auto px-1 py-1 text-sm font-normal text-gray-700 bg-white  "
                      >
                        Transaction Charge GL{" "}
                        <span className="text-red-600 px-1 ">*</span>
                      </label>
                      <select
                        name="transactionChargeAccountType"
                        id="transactionChargeAccountType"
                        value={transactionChargeAccountType}
                        onChange={(e) =>
                          settransactionChargeAccountType(e.target.value)
                        }
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option className="capatlize text-xs">
                            {data.strGLAccountType}-{data.strGLAccountNumber}
                          </option>
                        ))}
                      </select>
                      {error && transactionChargeAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Late Payment GL!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="GL Account Type"
                        className="block w-auto px-1 py-1 text-sm font-normal text-gray-700 bg-white mr-20    "
                      >
                        Card GL <span className="text-red-600 px-2 ">*</span>
                      </label>
                      <select
                        name="cardGlAccountType"
                        id="cardGlAccountType"
                        value={cardGlAccountType}
                        onChange={(e) => setcardGlAccountType(e.target.value)}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option className="capatlize text-xs">
                            {data.strGLAccountType}-{data.strGLAccountNumber}
                          </option>
                        ))}
                      </select>
                      {error && cardGlAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Card GL!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  </div>
                </div>
                <div className="bg-gray-100 px-4 py-1 text-right sm:px-2">
                  <button
                    title="Click Submit Button"
                    type="submit"
                    data-modal-toggle="defaultModal"
                    className="inline-flex justify-center  bg-blue-500 hover:bg-blue-700   focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 border-blue-700 py-1.5 px-4 mx-2   text-xs font-bold text-white rounded "
                  >
                    Submit
                  </button>
                  <button
                    title="Clear Data"
                    type="button"
                    onClick={handleClick}
                    className="inline-flex justify-center  bg-rose-500 hover:bg-rose-700 b  focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 border-rose-700 py-1.5 px-4 mx-2   text-xs font-bold text-white rounded "
                  >
                    Clear
                  </button>
                  <button
                    title="open modal"
                    type="button"
                    onClick={openModal}
                    className="bg-green-500 hover:bg-green-700 focus:ring-2 focus:ring-green-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-green-700 rounded"
                  >
                    View
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>

      {/* Your existing code here */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={() => {}}>
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
          <div className="fixed inset-0 overflow-hidden mt-4">
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
                <Dialog.Panel className="w-auto transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-md font-medium leading-6 text-white bg-indigo-500 flex justify-center "
                  >
                    View Credit Card Configuration GL
                  </Dialog.Title>
                  <div
                    className="cursor-pointer absolute right-0 mt-3.5 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out top-2"
                    onClick={closeModal}
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      aria-label="Close"
                      className="icon icon-tabler icon-tabler-x"
                      width={10}
                      height={10}
                      viewBox="0 0 24 24"
                      strokeWidth="2.5"
                      stroke="#dc2626"
                      fill="none"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    >
                      <path stroke="none" d="M0 0h24v24H0z" />
                      <line x1={18} y1={6} x2={6} y2={18} />
                      <line x1={6} y1={6} x2={18} y2={18} />
                    </svg>
                  </div>
                  <div className="overflow-x-auto relative shadow-md  ">
                    <div className="table-wrp block max-h-[28 rem] ">
                      <table className="w-full text-sm text-left text-black dark:text-blue-100">
                        <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                          <tr>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              Account Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              Interest GL Account Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              Intrest GL Account No
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              GST GL Account Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              GST GL Account No
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              Late Payment GL Account Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-2 whitespace-nowrap"
                            >
                              Late Payment GL Account No
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {resdata.length > 0 ? (
                            <>
                              {resdata.map((item) => (
                                <tr
                                  // key={uuidv4()}
                                  className="border-b dark:border-neutral-500"
                                >
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.strAccountType}
                                    </div>
                                  </td>
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.intrestGlAccountType || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.intrestGlAccountNo || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.gstGlAccountType || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.gstGlAccountNo || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.latePaymentGlAccountType || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.latePaymentGlAccountNo || "_"}
                                    </div>
                                  </td>
                                </tr>
                              ))}
                            </>
                          ) : (
                            <tr>
                              <td colSpan="6" className="px-4 py-4 text-center">
                                <span className="text-lg font-medium text-gray-500">
                                  No Data Available.
                                </span>
                              </td>
                            </tr>
                          )}
                        </tbody>
                      </table>
                    </div>
                  </div>
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
    </AppLayout>
  );
}
