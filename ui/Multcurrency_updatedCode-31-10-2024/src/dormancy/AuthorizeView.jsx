import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../layout/AppLayout";
import amsApi from "../api/amsApi";
import { Dialog, Transition } from "@headlessui/react";
import { useParams } from "react-router-dom";
import CustomAlert from "../layout/CustomAlert";
import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import { useNavigate } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
export default function AuthorizeView() {
  const [reason, setReason] = useState("");

  const { accountNumber } = useParams();
  let useid = localStorage.getItem("userName");

  const [data, setData] = useState({});
  let accountType = data.accountType;
  let creationDate = data.creationDate;
  let lastTxnDate = data.lastTxnDate;
  let closingBalance = data.closingBalance;
  let dormantMarkedDate = data.dormantMarkedDate;
  let status = data.status;
  let accountHolderName = data.accountHolderName;
  let AccountNumber = data.accountNumber;

  // let accountType = data.accountType;
  const navigate = useNavigate();
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

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

  useEffect(() => {
    checkervalidation();
  }, []);

  const checkervalidation = async () => {
    try {
      const response = await amsApi.post(
        `dormancy/checkervalidation`,
        {
          strAccountNumber: accountNumber,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setData(response.data.dormantAccountInformation);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
    }
  };

  const [list, setList] = useState([]);
  const DStatus = async () => {
    try {
      const response = await amsApi.post(
        `dormancy/status`,
        {
          accountNumber: accountNumber,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setList(response.data.dormantAccountMaster);
        openModal();
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
    // }
  };

  // Authorise

  const checkerProcess = async () => {
    // if (accountno === "" || accountno.length === 0) {
    //   setError(true);
    // } else {
    try {
      const response = await amsApi.post(
        `dormancy/checkerProcess`,
        {
          status: "Authorized",
          reason: reason,
          accountNumber: accountNumber,
          checkerId: useid,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        handleShowSuccess(response.data.message);
        setData(response.data);

        // setError("");
        Clear();
      } else {
        handleShowError(response.data);
      }
    } catch (error) {
      handleShowError(error.response.data);
    }
    // }
  };

  // Authorise

  const Reject = async () => {
    // if (accountno === "" || accountno.length === 0) {
    //   setError(true);
    // } else {
    try {
      const response = await amsApi.post(
        `dormancy/checkerProcess`,
        {
          status: "Reject",
          reason: reason,
          accountNumber: accountNumber,
          checkerId: useid,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        handleShowSuccess(response.data.message);
        setData(response.data);
        Clear();
      } else {
        handleShowError(response.data);
      }
    } catch (error) {
      handleShowError(error.response.data);
    }
    // }
  };

  let [isOpen, setIsOpen] = useState(false);

  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    setIsOpen(true);
  }

  const Clear = () => {
    setReason("");
    setData("");
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal  ">
                Authorize Dormancy view
              </p>
              <button
                title="Go Back"
                type="button"
                onClick={() => navigate("/authorise dormant")}
                className="inline-flex justify-center  rounded-md border border-transparent bg-gray-500 py-1 px-4 mx-2 text-xs font-sm text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
              >
                <ArrowLeftIcon className="h-3 w-3 " />
              </button>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1 mt-4 ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4 py-1 sm:p-2 bg-white  ">
                <div className="grid gap-6  md:grid-cols-4 px-8 ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="A to Z"
                      type="text"
                      id=" Name"
                      defaultValue={accountHolderName}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Name"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>

                  <div className="">
                    <label
                      htmlFor="Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Type
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Account Type"
                      type="text"
                      id="Account Type"
                      defaultValue={accountType}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Type"
                      autoComplete="off"
                    />
                    {/* {error && description.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Desc!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="Account Opened Date"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Opened Date
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Account Opend Date"
                      type="text"
                      id="AccountOpenedDate"
                      defaultValue={creationDate}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Opened Date"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="Last Txn Date"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Last Txn Date
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Last Txn Date"
                      type="text"
                      id="Last Txn Date"
                      defaultValue={lastTxnDate}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Last Txn Date"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="Account Balance"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Balance
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Account Balance"
                      type="text"
                      id="Account Balance"
                      defaultValue={closingBalance}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Balance"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="AccountDormantMarkedDate"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Dormant Marked Date
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Account Dormant Marked Date"
                      type="text"
                      id="AccountDormantMarkedDate"
                      defaultValue={dormantMarkedDate || ""}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Dormant Marked Date"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="AccountStatus"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Status
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Account Status"
                      type="text"
                      id="AccountStatus"
                      defaultValue={status}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Status"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>

                  <div>
                    <label
                      htmlFor="AccountStatus"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Number
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="accountnumber"
                      type="text"
                      id="accountnumber"
                      defaultValue={AccountNumber}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Status"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="PerviousDormantStatus"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Previous Dormant Status
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <button
                      title="Pervious Dormant Status"
                      type="text"
                      id="PerviousDormantStatus"
                      onClick={DStatus}
                      className="block w-full px-3 py-1 text-sm font-normal text-blue-600 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Pervious Dormant Status"
                      autoComplete="off"
                    >
                      {" "}
                      view previous dormant status
                    </button>
                  </div>
                  <div className="col-span-2">
                    <label
                      htmlFor="Reason"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Reason
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <textarea
                      title="Reason"
                      type="text"
                      id="Reason"
                      value={reason}
                      // maxLength={1}
                      // minLength={1}
                      onChange={(e) => setReason(e.target.value)}
                      // onBlur={(e) => setCharge(e.target.value.toUpperCase())}
                      className="block w-full px-3 py-2 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Reason"
                      autoComplete="off"
                    />
                    {/* {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 text-right sm:px-2 py-1.5 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  onClick={checkerProcess}
                  data-modal-toggle="defaultModal"
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Authorized
                </button>

                <button
                  title="Clear Data"
                  type="button"
                  onClick={Reject}
                  className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
                >
                  Reject
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {/* Dilog box */}
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

          <div className="fixed inset-0 overflow-hidden">
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
                    className="text-md font-medium leading-6 text-blue-900"
                  >
                    previous dormant status
                  </Dialog.Title>
                  <div className="overflow-x-auto relative shadow-md  ">
                    <div className="table-wrp block max-h-[27rem] ">
                      <table className="w-full text-xs text-left text-black dark:text-blue-100">
                        <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                          <tr>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Account Number
                            </th>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Account Opene Date
                            </th>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Account Dormant Date
                            </th>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Account Balance
                            </th>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Account Dormant Released Date
                            </th>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Dormant Released Reason
                            </th>
                            <th
                              scope="col"
                              className="py-2 px-2 whitespace-nowrap"
                            >
                              Release By
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {list.length > 0 ? (
                            <>
                              {list.map((item) => (
                                <tr
                                  key={uuidv4()}
                                  className="border-b dark:border-neutral-500"
                                >
                                  <td className="px-2 py-2 whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.accountNumber}
                                    </div>
                                  </td>
                                  <td className="px-2 py-2 whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.accountOpenedDate || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-2 whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.accountDormantDate || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-2 text-end whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.accountBalance}
                                    </div>
                                  </td>

                                  <td className="px-2 py-2 whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.accountDormantReleasedDate || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-2 whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.dormantReleasedReason || "_"}
                                    </div>
                                  </td>
                                  <td className="px-2 py-2 whitespace-nowrap">
                                    <div className="text-xs font-medium text-gray-900">
                                      {item.releasedBy || "_"}
                                    </div>
                                  </td>
                                </tr>
                              ))}
                            </>
                          ) : (
                            <tr>
                              <td colSpan="6" className="px-6 py-4 text-center">
                                <span className="text-lg font-medium text-gray-500">
                                  No data available.
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

      {/* Your existing code here */}

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
