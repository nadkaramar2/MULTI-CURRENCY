import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect, Fragment } from "react";
import CustomAlert from "../../layout/CustomAlert";
import { Dialog, Transition } from "@headlessui/react";
export default function GSTTypeMaster() {
  const [error, setError] = useState("");
  const [alldata, setalldata] = useState([]);
  const [gstType, setgstType] = useState("");
  const [glAccountType, setglAccountType] = useState("");
  const [gstPercentage, setgstPercentage] = useState("");
  const [gstDescription, setgstDescription] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  let participantID = sessionStorage.getItem("Participantid");
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
  // Vat Type List
  useEffect(() => {
    gstttypelist();
  }, []);
  const gstttypelist = async () => {
    try {
      const response = await amsApi.get(`tcsTypeMaster/getGlType`);
      if (response.data.code === "S0000") {
        setalldata(response.data.listData);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // save form
  const feetypeSubmit = async (event) => {
    event.preventDefault();
    if (gstType === "" || gstDescription === "" || glAccountType === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`gstTypMaster/addGstTypeMaster`, {
          participantId: participantID,
          gstType: gstType,
          gstPercentage: gstPercentage,
          gstDescription: gstDescription,
          glAccountType: glAccountType,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          setgstType("");
          setgstDescription("");
          setglAccountType("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };
  const handleClick = () => {
    setgstType("");
    setgstDescription("");
    setglAccountType("");
  };

  // demo
  // View GST Type
  const [resdata, setResData] = useState("");
  useEffect(() => {
    viewgst();
  }, []);

  const viewgst = async () => {
    try {
      const response = await amsApi.post(
        `gstTypMaster/viewGST`,
        {},

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setResData(response.data.gstData);
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

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-sm text-black :text-2xm  leading-normal py-1">
                GST Type Master
              </p>
            </div>
          </div>
        </div>
        <div className=" bg-white sm:rounded-md">
          <form className="" onSubmit={feetypeSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4 py-2 sm:p-2 bg-white">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-8">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-10 py-1 text-base font-normal text-gray-700 bg-white  "
                    >
                      GST Type<span className="text-red-600 px-3">*</span>
                    </label>
                    <input
                      type="text"
                      id="gstType"
                      value={gstType}
                      onChange={(e) => setgstType(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter GST Type"
                    />
                    {error && gstType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter GST Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-1  text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      GST Description{" "}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="gstDescription"
                      name="gstDescription"
                      value={gstDescription}
                      disabled={!gstType}
                      onChange={(e) => setgstDescription(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter GST  Description"
                      autoComplete="off"
                    />
                    {error && gstDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-1  text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      GST Percentage{" "}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="gstPercentage"
                      name="gstPercentage"
                      disabled={!gstDescription}
                      Add
                      Channel
                      value={gstPercentage + "%"}
                      onChange={(e) => {
                        const inputValue = e.target.value.replace("%", "");
                        if (
                          inputValue === "" ||
                          (parseFloat(inputValue) >= 0 &&
                            parseFloat(inputValue) <= 50)
                        ) {
                          setgstPercentage(inputValue);
                        }
                      }}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter GST Percentage"
                      autoComplete="off"
                    />
                    {error && gstPercentage.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Percentage
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1  text-base font-normal text-gray-700 bg-white  "
                    >
                      GL Account Link{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="glAccountType"
                      id="glAccountType"
                      value={glAccountType}
                      disabled={!gstDescription}
                      onChange={(e) => setglAccountType(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.strGLAccountType}
                        >
                          {data.strGLAccountType || ""}
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
                </div>
              </div>
              <div className="bg-gray-100 px-2 py-2 text-right sm:px-1 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-4  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-4 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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
                    className="text-md font-medium leading-6 text-white bg-indigo-500 flex justify-center "
                  >
                    View GST Type
                  </Dialog.Title>
                  <div
                    className="cursor-pointer absolute top-0 right-0 mt-3.5 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out top-2"
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
                              GST Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              GST Description
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              GST Percentage
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              GL Account Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              GL Account No
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              Created Date
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
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.gstType}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.gstDescription || "_"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.gstPercentage || "_"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.glAccountType || "_"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.glAccountNumber || "_"}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.createdDate || "_"}
                                    </div>
                                  </td>
                                </tr>
                              ))}
                            </>
                          ) : (
                            <tr>
                              <td colSpan="6" className="px-6 py-4 text-center">
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
