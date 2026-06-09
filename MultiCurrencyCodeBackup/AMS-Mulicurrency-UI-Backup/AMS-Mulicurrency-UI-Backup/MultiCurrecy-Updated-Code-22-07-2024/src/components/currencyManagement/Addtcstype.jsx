import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { Dialog, Transition } from "@headlessui/react";
export default function Addtcstype() {
  const [glaccounttype, setGlaccounttype] = useState("");
  const [tcstype, setTcstype] = useState("");
  const [tcsdescription, setTcsDescription] = useState("");
  const [data, setData] = useState([]);
  const [error, setError] = useState("");
  let useid = localStorage.getItem("userName");

  // const Description = alldata2.map((t) => [t.strGLAccountType]);
  // const glAccountType = alldata2.strGLAccountType;
  console.log(data);

  let participantID = sessionStorage.getItem("Participantid");
  const handleClick = () => {
    setTcsDescription("");
    setTcstype("");
    setGlaccounttype("");
  };
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

  // GL Account Creation List
  useEffect(() => {
    gLAccountlist();
  }, []);

  const gLAccountlist = async () => {
    try {
      const response = await amsApi.get(`tcsTypeMaster/getGlType`, {});

      if (response.data.code === "S0000") {
        setData(response.data.listData);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // submit
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (tcstype === "" || tcsdescription === "" || glaccounttype === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`tcsTypeMaster/addTcsType`, {
          // strparticipantid: participantID,
          // strtcstype: tcstype,
          // strdescription: tcsdescription,
          // strglaccountType: glaccounttype,
          // strcreatedby: useid,

          participantId: participantID,
          tcsType: tcstype,
          description: tcsdescription,
          glAccountType: glaccounttype,
          strcreatedby: useid,
          // strcreateddate: "2023-07-26",
        });
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

  const [isOpen, setIsOpen] = useState(false);

  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };

  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };
  // View tcs Type
  const [resdata, setResData] = useState("");
  useEffect(() => {
    viewtcs();
  }, []);

  const viewtcs = async () => {
    try {
      const response = await amsApi.post(
        `tcsTypeMaster/viewTcsType`,
        {},

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setResData(response.data.tcsTypeData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Add Tcs Type
              </p>
            </div>
          </div>
        </div>
        <div className=" bg-white sm:rounded-md">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4  sm:p-2 bg-white">
                <div className="grid gap-2 lg:grid-cols-1 md:grid-cols-1 px-4 ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-12 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Tcs Type<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="tcstype"
                      value={tcstype}
                      onChange={(e) => setTcstype(e.target.value.toUpperCase())}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Tcs Type"
                    />
                    {error && tcstype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter tcs type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-2 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Tcs Description
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      type="text"
                      id="tcsdescription"
                      name="tcsdescription"
                      value={tcsdescription}
                      disabled={!tcstype}
                      onChange={(e) => setTcsDescription(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Tcs Percentage "
                      autoComplete="off"
                    />
                    {error && tcsdescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Tcs Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-1 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Gl AccountType
                      <span className="text-red-600">*</span>
                    </label>
                    <select
                      id="glaccounttype"
                      name="glaccounttype"
                      disabled={!tcsdescription}
                      value={glaccounttype}
                      onChange={(e) => setGlaccounttype(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {data.map((item) => (
                        <option value={item.strGLAccountType}>
                          {item.strGLAccountType || ""}
                        </option>
                      ))}
                    </select>
                    {error && glaccounttype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select GL Account Type
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
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
                >
                  Clear
                </button>
                <button
                  title="open modal"
                  type="button"
                  onClick={openModal}
                  className="bg-green-500 hover:bg-green-700 focus:ring-2 focus:ring-green-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-green-700 rounded"
                >
                  View Network Type
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
                    View TCS Type
                  </Dialog.Title>
                  <div
                    className="cursor-pointer absolute top-0 right-0 mt-2.5 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out top-2"
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
                              TCS Type
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              Description
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
                                      {item.tcsType}
                                    </div>
                                  </td>
                                  <td className="px-6 py-1.5 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">
                                      {item.description || "_"}
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
