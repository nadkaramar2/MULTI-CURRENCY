import React from "react";
import AppLayout from "../../layout/AppLayout";
import { useState, Fragment, useEffect } from "react";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { Dialog, Transition } from "@headlessui/react";
import { v4 as uuidv4 } from "uuid";
export default function Currencyconversion() {
  const [fromCuurency, setFromCuurency] = useState("");
  const [toCurrency, setToCurrency] = useState("");
  const [fromCurrencyValue, setFromCurrencyValue] = useState("");
  const [toCurrencyValue, setToCurrencyValue] = useState("");
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
    setFromCuurency("");
    setToCurrency("");
    setFromCurrencyValue("");
    setToCurrencyValue("");
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      fromCuurency === "" ||
      toCurrency === "" ||
      fromCurrencyValue === "" ||
      toCurrencyValue === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `currency_ConversionController/addCurrencyConversion`,
          {
            fromcurrency: fromCuurency,
            tocurrency: toCurrency,
            fromcurrencyvalue: fromCurrencyValue,
            toCurrencyConversion: toCurrencyValue,
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

  const [isOpen, setIsOpen] = useState(false);

  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };

  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };

  // View Currency conversion
  const [resdata, setResData] = useState("");
  useEffect(() => {
    ViewResponse();
  }, []);

  const ViewResponse = async () => {
    try {
      const response = await amsApi.post(
        `currency_ConversionController/viewCurrencyConversion`,

        {},

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setResData(response.data.currencyConversionData);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError();
      // "Sorry, the server is under mentaines. Please try again later"
    }
  };

  const [editingRow, setEditingRow] = useState(null);
  const startEdit = (id) => {
    setEditingRow(id);
  };
  const handleEditChange = (e, id, field) => {
    const updatedData = resdata.map((data) => {
      if (data.id === id) {
        return { ...data, [field]: e.target.value };
      }
      return data;
    });
    setResData(updatedData);
  };

  const handleUpdate = async (id) => {
    try {
      const updatedRow = resdata.find((data) => data.id === id);
      const response = await amsApi.post(
        `currency_ConversionController/updateCurrencyConversion`,
        {
          fromcurrency: updatedRow.fromcurrency,
          tocurrency: updatedRow.tocurrency,
          fromcurrencyvalue: updatedRow.fromcurrencyvalue,
          toCurrencyConversion: updatedRow.toCurrencyConversion,
        },
        {
          headers: {
            "Content-type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        handleShowSuccess(response.data.message);
        setEditingRow(null);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError();
      // "Sorry, the server is under mentaines. Please try again later"
    }
  };
  const cancelEdit = () => {
    setEditingRow(null);
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Currency Conversion
              </p>
            </div>
          </div>
        </div>

        <div className="md:col-span-2 lg:col-span-1 sm:rounded-md bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4  sm:p-2 bg-white  ">
                <div className="grid gap-2  px-4  md:grid-cols-1 ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-12 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      From Currency
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      //   minLength={1}
                      //   maxLength={3}
                      type="text"
                      id="fromCuurency"
                      value={fromCuurency}
                      onChange={(e) =>
                        setFromCuurency(e.target.value.toUpperCase())
                      }
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter From Currency"
                    />
                    {error && fromCuurency.length <= 0 ? (
                      <p className="text-red-500  mt-4 text-xs font-medium">
                        Please Enter from Cuurency
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-2 text-sm font-normal text-gray-700 bg-white  "
                    >
                      From Currency Value
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      //   minLength={1}
                      //   maxLength={3}
                      type="text"
                      id="fromCurrencyValue"
                      disabled={!fromCuurency}
                      value={fromCurrencyValue}
                      onChange={(e) => setFromCurrencyValue(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter From Currency Value"
                    />
                    {error && fromCurrencyValue.length <= 0 ? (
                      <p className="text-red-500 mt-4  text-xs font-medium">
                        Please Enter from CurrencyValue
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-16 text-sm font-normal text-gray-700 bg-white  "
                    >
                      To Currency
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      //   minLength={1}
                      //   maxLength={3}
                      type="text"
                      id="toCurrency"
                      disabled={!fromCurrencyValue}
                      value={toCurrency}
                      onChange={(e) =>
                        setToCurrency(e.target.value.toUpperCase())
                      }
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter To Currency"
                    />
                    {error && toCurrency.length <= 0 ? (
                      <p className="text-red-500  mt-4 text-xs font-medium">
                        Please Enter To Currency
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-7 text-sm font-normal text-gray-700 bg-white  "
                    >
                      To Currency Value
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      //   minLength={1}
                      //   maxLength={3}
                      type="text"
                      id="toCurrencyValue"
                      disabled={!toCurrency}
                      value={toCurrencyValue}
                      onChange={(e) => setToCurrencyValue(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter To Currency Value"
                    />
                    {error && toCurrency.length <= 0 ? (
                      <p className="text-red-500  mt-4 text-xs font-medium">
                        Please Enter To CurrencyValue
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4  py-1 text-right sm:px-2 ">
                <button
                  type="button"
                  title="Click Submit Button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Submit
                </button>
                <button
                  type="button"
                  title="Clear Data"
                  data-modal-toggle="defaultModal"
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
                  View
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
      {/* Your existing code here */}

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
                    View Currency Conversion
                  </Dialog.Title>
                  <div
                    className="cursor-pointer absolute top-0 right-0 mt-2.5 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out "
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
                              from currency
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              to currency
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              from currency value
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              to currency value
                            </th>
                            <th
                              scope="col"
                              className="py-1.5 px-6 whitespace-nowrap"
                            >
                              Action
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {resdata.length > 0 ? (
                            <>
                              {resdata.map(
                                ({
                                  fromcurrency,
                                  tocurrency,
                                  fromcurrencyvalue,
                                  toCurrencyConversion,
                                  id,
                                }) => (
                                  <tr
                                    key={id}
                                    className="border-b dark:border-neutral-500"
                                  >
                                    <td className="px-6 py-1.5 whitespace-nowrap">
                                      <div className="text-sm font-medium text-gray-900">
                                        {fromcurrency || "-"}
                                      </div>
                                    </td>
                                    <td className="px-6 py-1.5 whitespace-nowrap">
                                      <div className="text-sm font-medium text-gray-900">
                                        {tocurrency || "-"}
                                      </div>
                                    </td>
                                    <td className="px-6 py-1.5 whitespace-nowrap">
                                      {editingRow === id ? (
                                        <input
                                          type="text"
                                          defaultValue={fromcurrencyvalue}
                                          onChange={(e) =>
                                            handleEditChange(
                                              e,
                                              id,
                                              "fromcurrencyvalue"
                                            )
                                          }
                                          className="border border-gray-300 text-gray-900 text-xs rounded-sm  block w-26 p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white"
                                        />
                                      ) : (
                                        <div className="text-sm font-medium text-gray-900">
                                          {fromcurrencyvalue || "-"}
                                        </div>
                                      )}
                                    </td>
                                    <td className="px-6 py-1.5 whitespace-nowrap">
                                      {editingRow === id ? (
                                        <input
                                          type="text"
                                          defaultValue={toCurrencyConversion}
                                          onChange={(e) =>
                                            handleEditChange(
                                              e,
                                              id,
                                              "toCurrencyConversion"
                                            )
                                          }
                                          className="border border-gray-300 text-gray-900 text-xs rounded-sm  block w-26 p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white"
                                        />
                                      ) : (
                                        <div className="text-sm font-medium text-gray-900">
                                          {toCurrencyConversion || "-"}
                                        </div>
                                      )}
                                    </td>
                                    <td className="whitespace-nowrap">
                                      {editingRow === id ? (
                                        <div>
                                          <button
                                            title="Click Update Button"
                                            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                                            onClick={() => handleUpdate(id)}
                                          >
                                            Update
                                          </button>
                                          <button
                                            title="Click Cancel Button"
                                            className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-2  mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                                            onClick={() => cancelEdit()}
                                          >
                                            Cancel
                                          </button>
                                        </div>
                                      ) : (
                                        <button
                                          title="Click Edit Button"
                                          type="button"
                                          onClick={() => startEdit(id)}
                                          className="px-6 py-2.5 whitespace-nowrap"
                                        >
                                          <svg
                                            className="w-5 h-5 ml-1"
                                            fill="currentColor"
                                            viewBox="0 0 20 20"
                                            xmlns="http://www.w3.org/2000/svg"
                                          >
                                            <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"></path>
                                            <path
                                              fillRule="evenodd"
                                              d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
                                              clipRule="evenodd"
                                            ></path>
                                          </svg>
                                        </button>
                                      )}
                                    </td>
                                  </tr>
                                )
                              )}
                            </>
                          ) : (
                            <tr>
                              <td colSpan="6" className="px-6 py-4 text-center">
                                <span className="text-sm font-medium text-gray-500">
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
