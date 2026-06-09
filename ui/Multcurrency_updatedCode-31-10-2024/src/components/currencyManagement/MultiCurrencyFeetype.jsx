import React, { useEffect, useState, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import CustomAlert from "../../layout/CustomAlert";
import amsApi from "../../api/amsApi";
import { Dialog, Transition } from "@headlessui/react";
import { v4 as uuidv4 } from "uuid";
export default function MultiCurrencyFeetype() {
  const [feeType, setFeeType] = useState("");
  const [feepercentage, setFeepercentage] = useState("");
  const [tcsType, setTcsType] = useState("");
  const [IsFlatFee, setIsFlatFee] = useState("");
  const [feeDescription, setFeeDescription] = useState("");
  const [feeamount, setFeeAmount] = useState("");
  const [ispercentage, setIspercentage] = useState("");
  const [glAccountType, setGlAccountType] = useState("");

  const [alldata2, setalldata2] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const [error, setError] = useState("");

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

  const handleClick = () => {
    setFeeType("");
    setFeepercentage("");
    setTcsType("");
    setIsFlatFee("");
    setFeeDescription("");
    setFeeAmount("");
    setIspercentage("");
    setGlAccountType("");
  };

  // GL Account Creation List
  useEffect(() => {
    gLAccountlist();
  }, []);

  const gLAccountlist = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/getGlAccTypeAccNumbr`,
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
  const [gstTypes, setGstTypes] = useState([]);
  console.log(gstTypes);

  useEffect(() => {
    getgst();
  }, []);

  const getgst = async () => {
    try {
      const response = await amsApi.get(`gstTypMaster/getGstTypeAll`, {});

      if (response.data.code === "S0000") {
        setGstTypes(response.data.gstTypeList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // GST type

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      feeType === "" ||
      feeDescription === "" ||
      IsFlatFee === "" ||
      // feeamount === "" ||
      // ispercentage === "" ||
      feepercentage === "" ||
      glAccountType === "" ||
      tcsType === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `multiCurrencyFeeType_Master/addMulticurrencyFeeTypeMaster`,
          {
            strParticipantId: participantID,
            feeType: feeType,
            feeDescription: feeDescription,
            isFlatFees: IsFlatFee,
            feeAmt: feeamount,
            isPercentageFee: ispercentage,
            percentageFee: feepercentage,
            glAccountType: glAccountType,
            gstType: tcsType,
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  // // when i clcik isdlatfee "Y" the automatic show the "N" in is percentage
  // useEffect(() => {
  //   if (IsFlatFee === "Y") {
  //     setIspercentage("N");
  //   } else if (IsFlatFee === "N") {
  //     setIspercentage("Y");
  //   }
  // }, [IsFlatFee]);

  const [isOpen, setIsOpen] = useState(false);

  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };

  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };
  // View fee type master
  const [resdata, setResData] = useState("");
  useEffect(() => {
    Viewfeetype();
  }, []);
  const Viewfeetype = async () => {
    try {
      const response = await amsApi.post(
        `feeTypeMaster/getFeeAccountTypeView`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setResData(response.data.viewFeeAccountTypelist);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError();
      // "Sorry, the server is under mentaines. Please try again later"
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                MultiCurrency Fee Type Master
              </p>
            </div>
          </div>
        </div>
        <div className=" bg-white sm:rounded-md">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4 py-2 sm:p-2 bg-white">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-4">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-14 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Fee Type
                      <span className="text-red-600 px-0.5">*</span>
                    </label>
                    <input
                      type="text"
                      id="feeType"
                      value={feeType}
                      onChange={(e) => setFeeType(e.target.value.toUpperCase())}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Fee Type"
                      autoComplete="off"
                    />
                    {error && feeType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Fee Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-5 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Fee Description
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      type="text"
                      id="feeDescription"
                      disabled={!feeType}
                      value={feeDescription}
                      onChange={(e) => setFeeDescription(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Fee Description"
                      autoComplete="off"
                    />
                    {error && feeDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Fee Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-16  py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Flat Fee
                      <span className="text-red-600 px-0.5">*</span>
                    </label>

                    <select
                      id="IsFlatFee"
                      name="IsFlatFee"
                      disabled={!feeDescription}
                      placeholder="Enter IsFlat Fee"
                      autoComplete="off"
                      value={IsFlatFee}
                      onChange={(e) => setIsFlatFee(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="Y">Yes</option>
                      <option value="N"> No</option>
                    </select>
                    {error && IsFlatFee.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter IsFlatFee
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  {IsFlatFee === "Y" && (
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 mr-6 py-1 text-sm font-normal text-gray-700 bg-white  "
                      >
                        Fee Amount
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="feeamount"
                        value={feeamount}
                        onChange={(e) => setFeeAmount(e.target.value)}
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Fee Description"
                        autoComplete="off"
                      />
                      {error && feeamount.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Fee Amount
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  )}
                  {IsFlatFee === "N" && (
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3  py-1 mr-2 text-sm font-normal text-gray-700 bg-white  "
                      >
                        Percentage Fee
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="ispercentage"
                        name="ispercentage"
                        // disabled={!feeamount}
                        placeholder="Enter Is percentage fee"
                        value={ispercentage}
                        onChange={(e) => setIspercentage(e.target.value)}
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        <option value="Y">Yes</option>
                        <option value="N"> No</option>
                      </select>
                      {error && ispercentage.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Is Percentage fee
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  )}
                  {IsFlatFee === "N" && (
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3  py-1 mr-3.5 text-sm font-normal text-gray-700 bg-white  "
                        // className="block text-xs font-semibold text-gray-700"
                      >
                        Fee Percentage
                        <span className="text-red-600 px-1">*</span>
                      </label>
                      <input
                        type="text"
                        id="feepercentage"
                        name="feepercentage"
                        value={feepercentage}
                        // disabled={!IsFlatFee}
                        onChange={(e) => setFeepercentage(e.target.value)}
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Fee percentage"
                        autoComplete="off"
                      />
                      {error && feepercentage.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter percentage fee
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  )}
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-1.5 text-sm font-normal text-gray-700 bg-white  "
                    >
                      GL Account Type
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <select
                      id="glAccountType"
                      name="glAccountType"
                      disabled={!feepercentage}
                      value={glAccountType}
                      onChange={(e) => setGlAccountType(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata2.map((data) => (
                        <option className="capatlize text-sm">
                          {data.strGLAccountType}-{data.strGLAccountNumber}
                        </option>
                      ))}
                    </select>
                    {error && glAccountType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select GL Account Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-11 text-sm font-normal text-gray-700 bg-white  "
                    >
                      GST Type
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="tcsType"
                      id="tcsType"
                      value={tcsType}
                      disabled={!glAccountType}
                      onChange={(e) => setTcsType(e.target.value.toUpperCase())}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {gstTypes.map((gstType) => (
                        <option key={gstType} value={gstType}>
                          {gstType}
                        </option>
                      ))}
                    </select>
                    {error && tcsType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select GST Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm py-1 px-2 border border-blue-700 rounded"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
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

      {/* dailog box */}
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

          <div className="fixed inset-0 overflow-hidden">
            <div className="flex min-h-full items-center justify-center p-4 text-center mt-1">
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
                    View Fee Type Master
                  </Dialog.Title>
                  <div
                    className="cursor-pointer absolute top-0 right-0 mt-4 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out "
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
                    <div className="table-wrp block max-h-[27rem] ">
                      <table className="w-full text-sm text-left text-black dark:text-blue-100">
                        <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                          <tr>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              fee Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              fee Description
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              fee Amt
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              isPercentage Fee
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              percentage Fee
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              glAccount Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              glAccount Number
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              gst Type
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {resdata.length > 0 ? (
                            <>
                              {resdata.map((item) => (
                                <tr
                                  key={uuidv4()}
                                  className="border-b dark:border-neutral-500"
                                >
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.feeType || "-"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.feeDescription || "-"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.feeAmt || "-"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.isPercentageFee || "-"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.percentageFee || "-"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.glAccountType || "-"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.glAccountNumber || "-"}
                                    </div>
                                  </td>{" "}
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.gstType || "-"}
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
